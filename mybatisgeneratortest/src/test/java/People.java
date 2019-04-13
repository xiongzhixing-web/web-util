public enum People {
    CHILD(1, "儿童"), STUDENT(2, "同学"), MAN(3, "人");

    private int key;
    private String val;

    private People(int key, String val) {
        this.key = key;
        this.val = val;
    }

    public int getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    public int getValByKey(String val) {
        for (People p : People.values()) {
            if (p.val.equalsIgnoreCase(val)) {
                return p.key;
            }
        }
        return -1;
    }
}
