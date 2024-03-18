import { Route, Navigate } from "react-router-dom";
import useUserStore from "../stores/userStore";

const hasRole = (requiredRole, userRole) => {
  return userRole === requiredRole;
};

const SecureRoute = ({ element, requiredRole, path }) => {
  const userRole = useUserStore((state) => state.user.role);
  return hasRole(requiredRole, userRole) ? (
    <Route path={path} element={element} />
  ) : (
    <Navigate to="/login" />
  );
};

export default SecureRoute;
