import React from "react";
import {Button} from "reactstrap";

// Täältä löytyy kaikenlaista:
// https://reactstrap.github.io/components/form/

class Body extends React.Component {

    render() {

        return (
            <div className="body" class="flex-body">
                <Button className = "button">
                    Lisää uusi tuote
                </Button>
            </div>
        );
    }
}

export default Body;