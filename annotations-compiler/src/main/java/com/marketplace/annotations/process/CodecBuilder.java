package com.marketplace.annotations.process;

import com.marketplace.annotations.MongoRecordValue;
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
import java.util.stream.Collectors;

public class CodecBuilder extends AbstractCodeBuilder{
    private final boolean wrapped;
    private final Filer filer;
    private final Element element;
    private final ClassName className;
    private final TypeName typeName;
    private List<? extends Element> recordComponents;

    //
//            List<? extends Element> recordComponents = element.getEnclosedElements()
//                    .stream()
//                    .filter(el -> el.getKind().equals(ElementKind.RECORD_COMPONENT))
//                    .collect(Collectors.toList());
//
//            recordComponents
//                    .forEach(component -> {
//                        Name simpleName = component.getSimpleName();
//                        TypeMirror typeMirror = component.asType();
//                        note(format("Type: %s, name: %s", typeMirror.toString(), simpleName.toString()));
//                    });

    public CodecBuilder(Filer filer, MongoRecordValue annotation, Element element) {
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
        MethodSpec.Builder builder = MethodSpec.methodBuilder("encode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(BsonWriter.class, "writer")
                .addParameter(typeName, "value")
                .addParameter(EncoderContext.class, "encoderContext");
//                .addStatement("writer.writeString(value.toString())");

        return builder.build();
    }


    private MethodSpec generateDecodeMethod() {

        List<ExecutableElement> executableElements = ElementFilter.constructorsIn(element.getEnclosedElements());
//        for (ExecutableElement executableElement : executableElements) {
//
//        }
//
        Optional<ExecutableElement> any = executableElements.stream()
                .filter(it -> it.getParameters().size() == recordComponents.size())
                .findAny();

        Optional<List<? extends VariableElement>> optionalParams = any.map(ExecutableElement::getParameters);
//
//        List<? extends VariableElement> variableElement = any.map(it -> it.getParameters());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("decode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeName)
                .addParameter(BsonReader.class, "reader")
                .addParameter(DecoderContext.class, "decoderContext");

        optionalParams.ifPresentOrElse(params -> {
            if (params.size() == 1) {
                if (wrapped) {

                } else {

                }
            } else {

            }
        }, () -> {

        });

//        variableElement.ifPresentOrElse(parameter -> {
//            TypeMirror typeMirror = parameter.asType();
//            builder.addStatement("String value = reader.readString()");
//            builder.beginControlFlow("if(value == null || value.isEmpty())")
//                    .addStatement("return null")
//                    .endControlFlow();
//            if (typeMirror.toString().equals("java.lang.String")) {
//                builder.addStatement("return new $T(value)", typeName);
//            } else if (typeMirror.toString().equals("java.util.UUID")) {
//                builder.addStatement("return new $T($T.fromString(value))", typeName, UUID.class);
//            }
//
//        }, () -> {
//            builder.addStatement("return null");
//        });

        builder.addStatement("return null");
        return builder.build();
    }

    public void generateCodecProvider() throws IOException {

    }

//    Float
//    Character
//    Boolean
//    Integer
//    Long
//    Double
//    String

// BigDecimal



    /*
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

    private MethodSpec generateEncodeMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("encode")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(BsonWriter.class, "writer")
                .addParameter(typeName, "value")
                .addParameter(EncoderContext.class, "encoderContext")
                .addStatement("writer.writeString(value.toString())");

        return builder.build();
    }



    public void generateCodecProvider() throws IOException {
        TypeVariableName tTypeVariable = TypeVariableName.get("T");
        TypeName codecProviderTypeName = TypeName.get(CodecProvider.class);
        ClassName codecProviderClassName = ClassName.get(Codec.class);
        ParameterizedTypeName returnTypeName = ParameterizedTypeName.get(codecProviderClassName, tTypeVariable);
        ClassName clzz = ClassName.get(Class.class);
        ParameterizedTypeName parameterizedTypeName =
                ParameterizedTypeName.get(clzz, tTypeVariable);

        TypeSpec.Builder coderProviderTypeBuilder = TypeSpec.classBuilder(format("%sCodecProvider", this.className.simpleName()))
                .addSuperinterface(codecProviderTypeName)
                .addModifiers(Modifier.PUBLIC);

        String parameterizedTypeVariable = "clzz";

        MethodSpec.Builder getBuilder =
                MethodSpec.methodBuilder("get")
                        .addTypeVariable(tTypeVariable)
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                .addMember("value", "$S", "unchecked" ).build())
                        .addParameter(parameterizedTypeName, parameterizedTypeVariable)
                        .addParameter(CodecRegistry.class, "codecRegistry")
                        .returns(returnTypeName);

        getBuilder.beginControlFlow("if ($L != $T.class)", parameterizedTypeVariable, typeName)
                .addStatement("return null")
                .endControlFlow()
                .addStatement("return (Codec<T>) new $TCodec()", typeName);

        coderProviderTypeBuilder.addMethod(getBuilder.build());


        JavaFile javaFile = JavaFile
                .builder(this.className.packageName(), coderProviderTypeBuilder.build())
                .build();

        javaFile.writeTo(filer);
    }
     */
}
