class Client {
    id;
    applicationName;
    identifier;
    secret;
    requiresConsent;
    grantTypes;
    scopes;
    redirectUris;

    construct(id, applicationName, identifier, secret, requiresConsent, grantTypes, scopes, redirectUris) {
        this.id = id;
        this.applicationName = applicationName;
        this.identifier = identifier;
        this.secret = secret;
        this.requiresConsent = requiresConsent;
        this.grantTypes = grantTypes;
        this.scopes = scopes;
        this.redirectUris = redirectUris;
    }
}

export default Client