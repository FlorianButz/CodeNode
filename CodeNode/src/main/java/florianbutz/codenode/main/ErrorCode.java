package florianbutz.codenode.main;

public enum ErrorCode {
	ParsingError(1), RessourceLoadFailure(2), PathNotSet(3), FileNotFound(4);
	
	private final int value;
    private ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
