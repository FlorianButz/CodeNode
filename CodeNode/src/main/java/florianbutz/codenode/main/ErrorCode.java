package florianbutz.codenode.main;

public enum ErrorCode {
	ParsingError(1), RessourceLoadFailure(2);
	
	private final int value;
    private ErrorCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
