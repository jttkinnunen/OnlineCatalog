import React from "react";
import './Navigation.css';
// Importing from reactstrap like this:
import { Collapse,
    Navbar,
    UncontrolledTooltip,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    InputGroup,
    InputGroupAddon,
    Button,
    FormGroup,
    Label,
    Input,
    Form,
    DropdownItem } from 'reactstrap';
import'./Navigation.css';

class Navigation extends React.Component {
    constructor(props) {
        super(props);

        this.toggleNavbar = this.toggleNavbar.bind(this);
        this.submitSearch = this.submitSearch.bind(this);
        this.state = {
            collapsed: true
        };
    }

    toggleNavbar() {
        this.setState({
            collapsed: !this.state.collapsed
        });
    }

    // Submit search
    submitSearch() {
        this.props.setView("articles")
    }

    // Add functions for click event checks

    render() {
        let adminTools;

        // Admin only Nav component visibility
        if (this.props.user.admin === true){
            adminTools = (
                <div>
                    <DropdownItem divider />
                    <DropdownItem onClick={() => {
                        this.props.getUsers();
                        this.props.setView("manage-users");
                    }}>
                        Käyttäjien hallinta (Admin)
                    </DropdownItem>
                    <DropdownItem onClick={() => this.props.setView("audit-log")}>
                        Tapahtumat (Admin)
                    </DropdownItem>
                </div>
            )
        }
        else {
            adminTools = <div></div>
        }


        if (this.props.current_view !== "login")
        return (
                <div className="container-fluid">
                    <Navbar color="light" light  expand="sm">

                        <UncontrolledDropdown className="ml-auto" navbar>

                            <DropdownToggle >
                                <svg xmlns="http://www.w3.org/2000/svg" className="hamburger" width="24" height="24" viewBox="0 0 24 24"
                                     fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                     stroke-linejoin="round" className="feather feather-menu">
                                    <line x1="3" y1="12" x2="21" y2="12"></line>
                                    <line x1="3" y1="6" x2="21" y2="6"></line>
                                    <line x1="3" y1="18" x2="21" y2="18"></line>
                                </svg>
                            </DropdownToggle>
                            <DropdownMenu left>
                                <DropdownItem onClick={() => {this.props.setView("profile-page")}}>
                                    Profiili
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("articles")}>
                                    Etusivu
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("add-article")}>
                                    Lisää tuote
                                </DropdownItem>

                                {adminTools}

                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <NavbarBrand href="#" className="mr-auto" onClick={() => this.props.setView("articles")}>&nbsp; Matsku &nbsp;</NavbarBrand>
                        <Form className = "nav-form">
                            <InputGroup>
                                <Input type="text" id="textSearch" placeholder="Hae tuotetta..." />
                                <InputGroupAddon addonType="append"><Button color="primary" onClick={() => this.submitSearch()}>Hae</Button></InputGroupAddon>
                            </InputGroup>

                            {/*<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                 fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                 stroke-linejoin="round" className="feather feather-search" type="submit" onClick={() => this.props.setView("articles")}>
                                <circle cx="11" cy="11" r="8"/>
                                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                            </svg>
                            <Button className="btn btn-success" type="submit" onClick={() => this.props.setView("articles")}>Hae</Button>*/}
                        </Form>

                        <Nav classname="ml-auto" navbar>

                            <UncontrolledTooltip placement="bottom" target="TooltipTarget">
                                Kirjaudu ulos
                            </UncontrolledTooltip>
                                <NavItem>
                                    <NavLink onClick={() => this.props.setView("login")} id = "TooltipTarget">
                                        <svg xmlns="http://www.w3.org/2000/svg"  width="24" height="24" viewBox="0 0 24 24"
                                             fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                             stroke-linejoin="round" className="feather feather-unlock">
                                            <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                                            <path d="M7 11V7a5 5 0 0 1 9.9-1"/>
                                        </svg>

                                    </NavLink>
                                </NavItem>

                        </Nav>
                </Navbar>
            </div>

        );
        return (<div></div>);
    }
}




<<<<<<< HEAD
export default Navigation;

=======
export default Navigation;
>>>>>>> 8aa34a565ea7ebee3195e649c0128e3b2d7a781e
