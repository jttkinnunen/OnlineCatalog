import React from "react";

class Footer extends React.Component {

    render() {
        return (
            <div className="event-text">
                Current view: {this.props.current_view} Debug: {this.props.debugval}
            </div>
        );
    }
}

export default Footer;