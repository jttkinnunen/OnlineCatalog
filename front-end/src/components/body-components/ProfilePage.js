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
        return(
            <Form>
                <FormGroup row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <h6>Keijo Kepponen</h6>
                    </Col>
                </FormGroup>
                <FormGroup row>
                    <Col sm={{ size: 10, offset: 1 }}>
                        <h6>keijo.kepponen@fcgtalent.fi</h6>
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