package io.galacsh.starter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class Sample {

    private int id;
    private String name;

    @Override
    public String toString() {
        return "Sample{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sample sample = (Sample) o;
        return id == sample.id && Objects.equals(name, sample.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
