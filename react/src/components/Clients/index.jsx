import DataContext from "../../stores/DataContext"
import { Link } from "react-router-dom"
import style from './style.module.css'
import { useContext } from "react"

const Clients = () => {
    const dataContext = useContext(DataContext);

    return (
        <>
            <section className={style.container}>
                <div>
                    <h1>Clients</h1>
                    <Link to="register">Register</Link>
                </div>
                <div className={style.clientList}>
                    {dataContext.clients.map(client => (
                        <Client client={client} />
                    ))}
                </div>
            </section>
        </>
    )
}

const Client = ({client}) => {
    const route = `./${client.id}`;
    return (
        <div className={style.client}>
            <Link to={route}><div></div></Link>
            <Link to={route} className={style.name}>{client.applicationName}</Link>
        </div>
    )
}

export default Clients