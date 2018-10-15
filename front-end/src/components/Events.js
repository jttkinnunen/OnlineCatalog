import React from "react";
import {UncontrolledCollapse} from 'reactstrap';

// Kannattaa katsoa navbaria ja sen collapse ominaisuutta ainakin.
// https://reactstrap.github.io/components/navbar/

class Events extends React.Component {
    constructor(props) {
        super(props);

        this.toggle = this.toggle.bind(this);
        this.state = {
            isOpen: true,
        };
    }

    // Function for toggling collapse of events element
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
    return (
        <div className = "event-list" id="toggler">
            <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
            <UncontrolledCollapse toggler="#toggler">
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
                <div className="event-text">12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45</div>
            </UncontrolledCollapse>
        </div>
    );

    }
}

export default Events;