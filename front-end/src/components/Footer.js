import React from "react";

class Footer extends React.Component {

    render() {
        return (
            <div className="event-text">
                Current view: {this.props.view} Debug: {this.props.debug}
            </div>
        );
    }
}

export default Footer;