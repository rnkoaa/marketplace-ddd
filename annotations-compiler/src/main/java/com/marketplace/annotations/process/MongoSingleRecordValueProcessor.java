package com.marketplace.annotations.process;

import com.google.auto.service.AutoService;
import com.marketplace.annotations.MongoRecordValue;
import com.marketplace.annotations.MongoSingleRecordValue;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@AutoService(Processor.class)
public class MongoSingleRecordValueProcessor extends AbstractProcessor {
    private Filer filer;

    private Types typeUtils;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MongoSingleRecordValue.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.RECORD) {
                error(format("Only classes can be annotated with @%s", MongoSingleRecordValue.class.getSimpleName()), element);
                return true; // Exit processing
            }

            MongoSingleRecordValue annotation = element.getAnnotation(MongoSingleRecordValue.class);

            try {
                SingleCodecBuilder codecBuilder = new SingleCodecBuilder(filer, annotation, element);
                codecBuilder.generate();

                // generate the corresponding codecProvider class
                new SingleCodecProviderBuilder(filer, element).generate();
            } catch (IOException e) {
                error(e.getMessage());
            }
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
//        return SourceVersion.latestSupported();
        return SourceVersion.RELEASE_15;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(MongoSingleRecordValue.class.getCanonicalName());
    }

    private void error(String message, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    private void checkForNoArgumentConstructor(TypeElement type) {
        for (ExecutableElement constructor :
                ElementFilter.constructorsIn(type.getEnclosedElements())) {
            List<? extends VariableElement> constructorParameters =
                    constructor.getParameters();
            if (constructor.getParameters().isEmpty()) {
                return;
            }
        }
    }

}
