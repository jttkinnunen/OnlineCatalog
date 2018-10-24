import React from "react";
import { Table, Input, Button } from 'reactstrap';

class UserManagement extends React.Component {
    constructor(props) {
        super(props);
    }


    // TODO: Input handling (selection of users)

    // TODO: Submit deletion

    // TODO: Add warning pop-up for deletion


    render() {

        return(
            <div>
                <Table striped bordered hover responsive size="sm" className = "usermanagement-body">
                    <thead>
                        <tr>
                            <th></th>
                            <th>#</th>
                            <th>Etunimi</th>
                            <th>Sukunimi</th>
                            <th>Sähköposti</th>
                            <th>Ylläpitäjä</th>
                        </tr>
                    </thead>
                    <tbody>{this.props.users.map(function(item, key) {

                        return (
                            <tr key = {key}>
                                <td>
                                    <Input addon type="checkbox" />
                                </td>
                                <td>{item.id}</td>
                                <td>{item.first_name}</td>
                                <td>{item.last_name}</td>
                                <td>{item.email}</td>
                                <td>{item.admin.toString()}</td>
                            </tr>
                        )
                    })}</tbody>
                </Table>
                <Button color="primary" onClick={() => this.props.setView("add-user")}>Lisää uusi käyttäjä</Button>
                <Button color="danger">Poista valitut</Button>
            </div>
        );
    }
}

export default UserManagement;