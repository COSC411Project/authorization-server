class Scope {
    key;
    value;

    constructor(key, value) {
        this.key = key;
        this.value = value;
    }

    clone() {
        const scope = new Scope();
        scope.key = this.key;
        scope.value = this.value;
        return scope;
    }
}

export default Scope