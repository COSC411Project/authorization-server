import DataContext from "../../stores/DataContext";
import style from './style.module.css'
import { useParams } from "react-router-dom";
import { useContext } from "react";

const Client = () => {
    const params = useParams();
    const id = Number.parseInt(params.id);

    const dataContext = useContext(DataContext);
    const client = dataContext.clients.filter(client => client.id === id)[0];

    return (
        <section className={style.container}>
            <h1>{client.applicationName}</h1>

            <div>
                <h2>Client ID</h2>
                <p>{client.identifier}</p>
            </div>

            <div className={style.secrets}>
                <div className={style.header}>
                    <h2>Client secrets</h2>
                    <button>Generate a new client secret</button>
                </div>
                
                <div>
                    
                </div>
            </div>

            <div>
                <h2>Redirect Uris:</h2>
                <ul>
                    {client.redirectUris.map(redirectUri => (
                        <li>{redirectUri}</li>
                    ))}
                </ul>
            </div>
        </section>
    )
}

export default Client;