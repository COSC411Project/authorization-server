class GrantType {
    key;
    value;

    constructor(key, value) {
        this.key = key;
        this.value = value;
    }

    clone() {
        const grantType = GrantType();
        grantType.key = this.key;
        grantType.value = this.value;
        return grantType;
    }
}

export default GrantType