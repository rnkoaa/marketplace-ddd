package com.marketplace.annotations.process;

import com.marketplace.annotations.MongoRecordValue;
import com.marketplace.annotations.MongoSingleRecordValue;
import com.squareup.javapoet.*;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class SingleCodecBuilder extends AbstractCodeBuilder {
    private final boolean wrapped;
    private final Filer filer;
    private final Element element;
    private final ClassName className;
    private final TypeName typeName;
    private List<? extends Element> recordComponents;

    public SingleCodecBuilder(Filer filer, MongoSingleRecordValue annotation, Element element) {
        this.wrapped = annotation.wrapped();
        this.filer = filer;
        this.element = element;
        TypeMirror classTypeMirror = element.asType();
        this.className = getName(classTypeMirror);
        this.typeName = TypeName.get(classTypeMirror);

        recordComponents = element.getEnclosedElements()
                .stream()
                .filter(el -> el.getKind().equals(ElementKind.RECORD_COMPONENT))
                .collect(Collectors.toList());
    }

    public void generate() throws IOException {
        ClassName codec = ClassName.get(Codec.class);
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(codec, typeName);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(this.className.simpleName() + "Codec")
                .addSuperinterface(parameterizedTypeName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(generateEncodeMethod())
                .addMethod(generateDecodeMethod())
                .addMethod(generateGetEncoderClass());

        JavaFile javaFile = JavaFile.builder(this.className.packageName(), typeBuilder.build())
                .build();

        javaFile.writeTo(filer);
    }

    private MethodSpec generateGetEncoderClass() {
        ClassName clzz = ClassName.get(Class.class);
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(clzz, typeName);

        MethodSpec.Builder builder = MethodSpec.methodBuilder("getEncoderClass")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(parameterizedTypeName)
                .addStatement("return $T.class", typeName);

        return builder.build();
    }

    private MethodSpec generateEncodeMethod() {
        Element element = recordComponents.get(0);
        Name simpleName = element.getSimpleName();
        TypeMirror typeMirror = element.asType();

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        if (typeMirror.toString().equals("java.lang.String")) {
            codeBlockBuilder.add("writer.writeString(value.$L())", simpleName.toString());
        } else if (typeMirror.toString().equals("java.util.UUID")) {
            codeBlockBuilder.add("writer.writeString(value.$L().toString())", simpleName.toString());
        }

        MethodSpec.Builder builder = MethodSpec.methodBuilder("encode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(BsonWriter.class, "writer")
                .addParameter(typeName, "value")
                .addParameter(EncoderContext.class, "encoderContext")
                .addStatement(codeBlockBuilder.build());

        return builder.build();
    }

    private MethodSpec generateDecodeMethod() {
        List<ExecutableElement> executableElements = ElementFilter.constructorsIn(element.getEnclosedElements());
        Optional<ExecutableElement> any = executableElements.stream()
                .filter(it -> it.getParameters().size() == 1)
                .findAny();

        Optional<? extends VariableElement> variableElement = any.map(it -> it.getParameters().get(0));

        MethodSpec.Builder builder = MethodSpec.methodBuilder("decode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeName)
                .addParameter(BsonReader.class, "reader")
                .addParameter(DecoderContext.class, "decoderContext");

        variableElement.ifPresentOrElse(parameter -> {
            TypeMirror typeMirror = parameter.asType();
            builder.addStatement("String value = reader.readString()");
            builder.beginControlFlow("if(value == null || value.isEmpty())")
                    .addStatement("return null")
                    .endControlFlow();
            if (typeMirror.toString().equals("java.lang.String")) {
                builder.addStatement("return new $T(value)", typeName);
            } else if (typeMirror.toString().equals("java.util.UUID")) {
                builder.addStatement("return new $T($T.fromString(value))", typeName, UUID.class);
            }

        }, () -> {
            builder.addStatement("return null");
        });

        return builder.build();
    }
}
