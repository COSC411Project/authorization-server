class RedirectUri {
    key;
    value;

    constructor(key, value) {
        this.key = key;
        this.value = value;
    }

    clone() {
        const redirectUri = new RedirectUri();
        redirectUri.key = this.key;
        redirectUri.value = this.value;
        return redirectUri;
    }
}

export default RedirectUri