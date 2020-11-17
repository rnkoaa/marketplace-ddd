package com.marketplace.annotations.process;

import com.marketplace.annotations.MongoSingleRecordValue;
import com.squareup.javapoet.*;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import javax.annotation.processing.Filer;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class SingleCodecProviderBuilder extends AbstractCodeBuilder {
    private final Filer filer;
    private final ClassName className;
    private final TypeName typeName;

    public SingleCodecProviderBuilder(Filer filer, Element element) {
        this.filer = filer;
        TypeMirror classTypeMirror = element.asType();
        this.className = getName(classTypeMirror);
        this.typeName = TypeName.get(classTypeMirror);
    }

    public void generate() throws IOException {
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
                                .addMember("value", "$S", "unchecked").build())
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

//
//
//    public void generateCodecProvider() throws IOException {
//
//    }
//     */
}
