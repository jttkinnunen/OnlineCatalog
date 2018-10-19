import React from "react";
import Articles from './body-components/Articles.js';
import Login from './body-components/Login.js';
import UserManagement from './body-components/UserManagement.js';
import ChangePassword from './body-components/ChangePassword.js';
import ProfilePage from './body-components/ProfilePage.js';
import ArticleDetailed from './body-components/ArticleDetailed.js';
import ForgotPassword from './body-components/ForgotPassword.js';
import AddArticle from './body-components/AddArticle.js';

// Täältä löytyy kaikenlaista:
// https://reactstrap.github.io/components/form/

class Body extends React.Component {

    render() {
        if (this.props.current_view === "articles")
            return (
                <Articles articles = {this.props.articles}/>
            );

        if (this.props.current_view === "add-article")
            return (
                <AddArticle/>
            );

        if (this.props.current_view === "login")
            return (
                <Login login = {this.props.login} />
            );

        if (this.props.current_view === "manage-users")
            return (
                <UserManagement/>
            );

        if (this.props.current_view === "change-pass")
            return (
                <ChangePassword/>
            );

        if (this.props.current_view === "profile-page")
            return (
                <ProfilePage setView = {this.props.setView} />
            );

        if (this.props.current_view === "article-detailed")
            return (
                <ArticleDetailed/>
            );

        if (this.props.current_view === "forgot-pass")
            return (
                <ForgotPassword/>
            );

        return(
            <div></div>
        );
    }
}

export default Body;