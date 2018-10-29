import React from "react";
import {Collapse, Button, Table} from 'reactstrap';

// Kannattaa katsoa navbaria ja sen collapse ominaisuutta ainakin.
// https://reactstrap.github.io/components/navbar/

class Events extends React.Component {
    constructor(props) {
        super(props);
        this.toggle = this.toggle.bind(this);
        this.state = {
            isOpen: false
        };
    }

    // Function for toggling collapse of events element
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen,
        });
    }

    render() {

        let table_content;

        if (this.state.isOpen === true)
        {table_content =
            <tbody>
                <tr>
                    <th scope="row">Tänään 16:32</th>
                    <td>Paita, FCG, Musta, M</td>
                    <td>Erkki esimerkki</td>
                    <td>Lisäys</td>
                    <td>Oulu</td>
                    <td>+12 kpl</td>
                </tr>
                <tr>
                    <th scope="row">Eilen 20:42</th>
                    <td>Paita, FCG, Musta, XL</td>
                    <td>Erkki esimerkki</td>
                    <td>Poisto</td>
                    <td>Oulu</td>
                    <td>-2 kpl</td>
                </tr>
                <tr>
                    <th scope="row">22.10.2018 20:42</th>
                    <td>Kynä, FCG, Punainen</td>
                    <td>Erkki esimerkki</td>
                    <td>Inventointi</td>
                    <td>Oulu</td>
                    <td>120 kpl</td>
                </tr>
            </tbody>
        }
        else
        {table_content =
        <tbody>
            <tr>
                <th scope="row">Tänään 16:32</th>
                <td>Paita, FCG, Musta, M</td>
                <td>Erkki esimerkki</td>
                <td>Lisäys</td>
                <td>Oulu</td>
                <td>+12 kpl</td>
            </tr>
        </tbody>
        }

        if (this.props.current_view === "articles")
        return (
            <div className = "event-list" onClick={this.toggle}>
                <Table className="event-text" striped responsive size="sm">
                        {table_content}
                </Table>
            </div>
        );
        return(<div></div>);
    }
}

export default Events;

/*
<tbody>{this.props.users.map(function(item, key) {

                        return (
                            <tr key = {key}>
                                <td>{event.time}</td>
                                <td>{event.description}</td>
                            </tr>
                        )
                    })}</tbody>
 */