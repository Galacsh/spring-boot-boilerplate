package io.galacsh.starter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * Sample properties.
 */
@Getter
@Setter
@ConfigurationProperties("sample")
public class SampleProperties {

    /**
     * This description will be shown in the metadata file.
     * Default value is 123.
     * [NOTE] Content after the first period will not be shown in IntelliJ IDEA's hint.
     */
    private int id = 123;

    /**
     * This description will be shown in metadata file.
     * Default value is "Default name",
     * [NOTE] Content after the first period will not be shown in IntelliJ IDEA's hint.
     */
    private String name = "Default name";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleProperties that = (SampleProperties) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
