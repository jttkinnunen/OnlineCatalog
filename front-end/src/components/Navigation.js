import React from "react";
import { slide as Menu } from 'react-burger-menu'
import '../css/Navigation.css';


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
import '../css/Navigation.css';
// import OffCanvasButton from "./content-components/OffCanvasButton";

class Navigation extends React.Component {
    constructor(props) {
        super(props);

        this.toggleNavbar = this.toggleNavbar.bind(this);
        this.submitSearch = this.submitSearch.bind(this);
        this.handleQueryChange = this.handleQueryChange.bind(this);
        this.state = {
            collapsed: true,
            query: "",
        };
    }

    toggleNavbar() {
        this.setState({
            collapsed: !this.state.collapsed
        });
    }

    handleQueryChange(event) {
        this.setState({
            query: event.target.value,
        })

        // Send query to app.js as well
        this.props.setQuery(event.target.value);
    }

    // Submit search
    submitSearch() {
        this.props.setQuery(this.state.query);
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

                    <DropdownItem onClick={() => this.props.setView("article-detailed")}>
                        Artikkelin tiedot (TESTI)
                    </DropdownItem>
                </div>
            )
        }
        else {
            adminTools = <div></div>
        }


        if (this.props.current_view !== "login" && this.props.current_view !== "activate-user" && this.props.current_view !== "forgot-pass")
        return (
                <div className="container-fluid">
                    <Navbar  expand="sm">

                            {/*<OffCanvasButton/>*/}
                        <UncontrolledDropdown className="left-off canvas toggle" id="outline" navbar>

                            <DropdownToggle className="hamback" >
                                <svg xmlns="http://www.w3.org/2000/svg" className="hamburger" width="24" height="24" viewBox="0 0 24 24"
                                     fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round"
                                     strokeLinejoin="round">
                                    <line x1="3" y1="12" x2="21" y2="12"/>
                                    <line x1="3" y1="6" x2="21" y2="6"/>
                                    <line x1="3" y1="18" x2="21" y2="18"/>
                                </svg>
                            </DropdownToggle>
                            <DropdownMenu left="true">
                                <DropdownItem onClick={() => {this.props.setView("profile-page")}}>
                                    Profiili
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("articles")}>
                                    Etusivu
                                </DropdownItem>
                                <DropdownItem onClick={() => this.props.setView("add-article")}>
                                    Lisää uusi tuote
                                </DropdownItem>

                                {adminTools}

                            </DropdownMenu>
                        </UncontrolledDropdown>
                        <NavbarBrand href="#" className="mr-auto" id="maintitle" onClick={() => this.props.setView("articles")}>&nbsp; Matsku &nbsp;</NavbarBrand>
                        <Form className = "nav-form" id="searchfield">
                            <InputGroup>
                                <Input type="text" id="textSearch" placeholder="Hae nimellä ..." value = {this.state.query} onChange = {this.handleQueryChange} />
                                {/*<InputGroupAddon addonType="append"><Button color="primary" onClick={() => this.submitSearch()}>Hae</Button></InputGroupAddon>*/}
                            </InputGroup>

                            {/*<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"
                                 fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                                 stroke-linejoin="round" className="feather feather-search" type="submit" onClick={() => this.props.setView("articles")}>
                                <circle cx="11" cy="11" r="8"/>
                                <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
                            </svg>
                            <Button className="btn btn-success" type="submit" onClick={() => this.props.setView("articles")}>Hae</Button>*/}
                        </Form>

                        <Nav navbar>

                            <UncontrolledTooltip placement="bottom" target="TooltipTarget">
                                Kirjaudu ulos
                            </UncontrolledTooltip>
                                <NavItem>
                                    <NavLink onClick={() => this.props.setView("login")} id = "TooltipTarget">
                                        <svg xmlns="http://www.w3.org/2000/svg"  width="24" height="24" viewBox="0 0 24 24"
                                             fill="none" stroke="currentColor"  stroke-width="2" stroke-linecap="round"
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




export default Navigation;

