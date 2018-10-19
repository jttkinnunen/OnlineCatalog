import React from "react";
import { Col, Button, Form, FormGroup, Card, CardImg} from 'reactstrap';

class ProfilePage extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit() {
        this.props.setView("change-password");
    }

    // TODO: Linkkaa
    render() {
        let userType;

        if (this.props.user.admin === "1")
            {userType = <h6>Ylläpitäjän oikeudet</h6>}
        else
            {userType = <h6>Peruskäyttäjän oikeudet</h6>}

        return(
            <Form>
                <FormGroup row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <h6>{this.props.user.first_name} {this.props.user.last_name}</h6>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <h6>{this.props.user.email}</h6>
                    </Col>
                </FormGroup>

                <FormGroup row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        {userType}
                    </Col>
                </FormGroup>

                <FormGroup check row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <Button onClick={this.handleSubmit}>Vaihda salasana</Button>
                    </Col>
                </FormGroup>
            </Form>
        );
    }
}

export default ProfilePage;