import Client from "../models/Client"

function mapToClient(clientDTO) {
    return new Client(clientDTO.id,
                      clientDTO.applicationName,
                      clientDTO.identifier,
                      null,
                      clientDTO.requiresConsent,
                      clientDTO.grantTypes,
                      clientDTO.scopes,
                      clientDTO.redirectUris);
}

export default {
    mapToClient: mapToClient
}