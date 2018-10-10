import React from "react";


// Kannattaa katsoa navbaria ja sen collapse ominaisuutta ainakin.
// https://reactstrap.github.io/components/navbar/

class Events extends React.Component {
    constructor(props) {
        super(props);

        this.toggle = this.toggle.bind(this);
        this.state = {
            collapsed: true
        };
    }

    // Function for toggling collapse of events element
    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        /* Render either the preview event or list of all events (NOT DONE)*/
        return (
            <div className="events-component" class = "flex-events">
                <div class = "event-text">
                    12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45
                </div>
            </div>
        );
    }
}

function Event(props) {
    return (
        <div className="flex-event">
            12.12.2018 -1 kpl  "Paita, hieno, XL" Keijo K. 20:45
        </div>
    );
}

export default Events;