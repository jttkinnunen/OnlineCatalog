import React from "react";
import { Table, Input, Button } from 'reactstrap';
import '../../css/UserManagement.css';
import '../../css/Text.css';
import Modal from 'react-responsive-modal';

class UserManagement extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            deletion_list: [], //this.props.users.length
            teststring: '',
            modal: false,
        };
        this.handleChange = this.handleChange.bind(this);
        this.requestDelete = this.requestDelete.bind(this);
        this.doDelete = this.doDelete.bind(this);
        this.modalToggle = this.modalToggle.bind(this);
    }

    // TODO: Input handling (selection of users)

    // TODO: Submit deletion

    // TODO: Add warning pop-up for deletion

    handleChange(event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        //const name = target.name;
        const id = target.id;

        // Creates new array for deletion_list without editing the original (immutability is important)
        // Takes the original subarray until "id-1", concats new value, concats subarray from (id+1) to (.length)
        //let newArr = this.state.deletion_checklist.slice(0,id-1).concat(value, this.state.deletion_checklist.slice(id+1,this.state.deletion_checklist.length));

        let newList;

        // Add to list if checkbox was checked
        if (value === true)
            newList = this.state.deletion_list.concat(id);
        else{
            // remove from the list if checkbox was unchecked
            newList = this.state.deletion_list;
            var index = newList.indexOf(id);
            if (index > -1) {
                newList.splice(index, 1);
            }
        }

        this.setState({
            teststring: id,
            deletion_list: newList,
        })
    }

    modalToggle() {
        this.setState({
            modal: !this.state.modal
        });
    }

    requestDelete(){
        if (this.state.deletion_list.length > 0){
            this.modalToggle();
        }
    }

    doDelete(){
        // TODO: Insert deletion call here
        this.props.deleteUsers(this.state.deletion_list);
        this.modalToggle();
    }


    render() {
        // Needed for accessing "this" in the map function below
        let self = this;

        return(
            <div className = "usermanagement-body">

                <Modal open={this.state.modal} onClose={this.modalToggle} center className = "modal-confirm-user-delete">
                        <h5> Oletko varma?</h5>
                        Haluatko varmasti poistaa valitut {this.state.deletion_list.length} käyttäjää? Tätä toimintoa ei voi peruuttaa.
                    <br/>
                    <br/>
                        <Button color="danger" onClick={this.doDelete}>Poista</Button>{' '}
                        <Button color="secondary" id="reverse" onClick={this.modalToggle}>Peruuta</Button>
                </Modal>

                <h5 className = "white-h5">Käyttäjien hallinta</h5>
                teststring: {this.state.teststring} length: {self.state.deletion_list.length}
                <Table striped bordered hover responsive size="sm" className = "usermanagement-table">
                    <thead>
                        <tr>
                            <th></th>
                            <th>#</th>
                            <th>Etunimi</th>
                            <th>Sukunimi</th>
                            <th>Sähköposti</th>
                            <th>Viimeisin toiminta</th>
                            <th>Kirjautunut</th>
                            <th>Ylläpitäjä</th>
                        </tr>
                    </thead>

                    <tbody>{this.props.users.map(function(item, key) {

                        return (
                            <tr key = {key}>
                                <td>
                                    <Input addon type="checkbox" id = {key} onChange = {self.handleChange} />
                                </td>
                                <td>{item.id}</td>
                                <td>{item.first_name}</td>
                                <td>{item.last_name}</td>
                                <td>{item.email}</td>
                                <td></td>{/*{item.last_activity}*/}
                                <td></td>{/*{item.last_login}*/}
                                <td>{item.admin.toString()}</td>
                            </tr>
                        )
                    })}</tbody>
                </Table>

                <Button id="addbtn" color="success" onClick={() => this.props.setView("add-user")}>Lisää uusi käyttäjä</Button>
                <Button id="deletebtn" color="danger" onClick={() => this.requestDelete()}>Poista valitut</Button>

            </div>
        );
    }
}

export default UserManagement;