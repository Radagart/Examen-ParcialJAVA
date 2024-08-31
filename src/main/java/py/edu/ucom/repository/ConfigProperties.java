package py.edu.ucom.repository;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class ConfigProperties {

    @ConfigProperty(name = "presupuestos.file.path", defaultValue = "src/main/resources/presupuestos.json")
    String filePath;

    public String getFilePath() {
        return filePath;
    }
}
