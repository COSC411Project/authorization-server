import OptionsContext from "./OptionsContext";
import { useState } from "react";

const OptionsContextProvider = ({children}) => {
    const [grantTypes, setGrantTypes] = useState(["AUTHORIZATION_CODE", "CLIENT_CREDENTIALS", "REFRESH_TOKEN"]);
    const [scopes, setScopes] = useState(["READ", "READ_WRITE"]);
    const [roles, setRoles] = useState(["USER", "DEV"]);

    const data = {
        grantTypes: grantTypes,
        scopes: scopes,
        roles: roles
    };

    return (
        <OptionsContext.Provider value={data}>
            {children}
        </OptionsContext.Provider>
    )
}

export default OptionsContextProvider