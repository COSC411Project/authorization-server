
class ClientRegistrationDTO {
    applicationName;
    grantTypes;
    scopeRequired;
    scopes;
    redirectUris;

    constructor(applicationName, grantTypes, scopeRequired, scopes, redirectUris) {
        this.applicationName = applicationName;
        this.grantTypes = grantTypes;
        this.scopeRequired = scopeRequired;
        this.scopes = scopes;
        this.redirectUris = redirectUris;
    }
}