import React from "react";
import Articles from './content-components/Articles.js';
import Login from './content-components/Login.js';
import UserManagement from './content-components/UserManagement.js';
import ChangePassword from './content-components/ChangePassword.js';
import ProfilePage from './content-components/ProfilePage.js';
import ArticleDetailed from './content-components/ArticleDetailed.js';
import ForgotPassword from './content-components/ForgotPassword.js';
import AddArticle from './content-components/AddArticle.js';
import AddUser from './content-components/AddUser.js';
import ActivateUser from './content-components/ActivateUser.js';
import AuditLog from './content-components/AuditLog.js';

// Täältä löytyy kaikenlaista:
// https://reactstrap.github.io/components/form/

class Content extends React.Component {

    render() {
        if (this.props.current_view === "articles")
            return (
                <Articles articles = {this.props.articles} setView = {this.props.setView}/>
            );

        if (this.props.current_view === "add-article")
            return (
                <AddArticle/>
            );

        if (this.props.current_view === "login")
            return (
                <Login login = {this.props.login} setView = {this.props.setView} login_state = {this.props.login_state} />
            );

        if (this.props.current_view === "manage-users")
            return (
                <UserManagement users = {this.props.users} setView = {this.props.setView} deleteUsers = {this.props.deleteUsers}/>
            );

        if (this.props.current_view === "change-pass")
            return (
                <ChangePassword/>
            );

        if (this.props.current_view === "profile-page")
            return (
                <ProfilePage user = {this.props.user} setView = {this.props.setView} />
            );

        if (this.props.current_view === "article-detailed")
            return (
                <ArticleDetailed/>
            );

        if (this.props.current_view === "forgot-pass")
            return (
                <ForgotPassword setView = {this.props.setView} forgotPassword = {this.props.forgotPassword} pass_reset_state = {this.props.pass_reset_state} />
            );

        if (this.props.current_view === "add-user")
            return (
                <AddUser addUser = {this.props.addUser} add_user_state = {this.props.add_user_state}/>
            );

        if (this.props.current_view === "activate-user")
            return (
                <ActivateUser setView = {this.props.setView}/>
            );
        if (this.props.current_view === "audit-log")
            return (
                <AuditLog setView = {this.props.setView}/>
            );

        return(
            <div></div>
        );
    }
}

export default Content;