package mike.data.mapper.core.mapping.raw;

public record RawField(String name, String type, String description, String optValue) {

    public RawField(String name, String type, String description) {
	this(name, type, description, null);
    }
}
