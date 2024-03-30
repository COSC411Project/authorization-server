
const SelectWithItems = ({id, display, dataKey, items, value, onChange}) => {
    return (
        <select id={id} name={id} value={value} onChange={onChange} data-key={dataKey}>
            <option hidden>{display}</option>
            {items.map(item => (
                <option key={item} value={item}>{item}</option>
            ))}
        </select>
    )
}

export default SelectWithItems