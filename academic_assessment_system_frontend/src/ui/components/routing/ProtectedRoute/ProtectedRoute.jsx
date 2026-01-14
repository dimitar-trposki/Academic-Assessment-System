import React from 'react';
import useAuth from "../../../../hooks/useAuth.js";
import {Navigate, Outlet} from "react-router";

const ProtectedRoute = ({role}) => {
    const {isLoading, user} = useAuth();

    if (isLoading)
        return null;

    if (user === null)
        return <Navigate to="/login" replace/>;

    // ако сакаш role-based за понатаму, ќе користиме user.role кога ќе ја земеш од /me
    if (role && user.role && user.role !== role)
        return <Navigate to="/login" replace/>;

    return <Outlet/>;
};

export default ProtectedRoute;