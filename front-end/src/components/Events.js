import React from "react";
import {Collapse, Button, Table} from 'reactstrap';
import '../css/Events.css';
import '../css/Text.css';
import ReactCSSTransitionGroup from 'react-addons-css-transition-group';

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

        let table_content, arrow;

        if (this.state.isOpen === true)
        {table_content =
            <>
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
                <br/>


                   {/* <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                         className="feather feather-arrow-up">
                        <line x1="12" y1="19" x2="12" y2="5"></line>
                        <polyline points="5 12 12 5 19 12"></polyline>

                    </svg>*/}



                </tbody>


            </>

        arrow =
            <div className = "events-arrow">
                <svg xmlns="http://www.w3.org/2000/svg" width="28" height="28" viewBox="0 0 24 24" fill="none"
                     stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                     className="feather feather-arrow-up-circle">
                    <circle cx="12" cy="12" r="10"></circle>
                    <polyline points="16 12 12 8 8 12"></polyline>
                    <line x1="12" y1="16" x2="12" y2="8"></line>
                </svg>
            </div>
        }
        else
        {
            table_content =
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

            arrow = ''
        }

        if (this.props.current_view === "articles")
        return (



            <div className = "event-list" onClick={this.toggle}>
                <Table className=" events-table event-text" striped hover borderless responsive size="sm">
                    {table_content}
                </Table>
                { arrow }
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