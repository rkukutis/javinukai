import { Link } from "react-router-dom";

function NavBar() {
  return (
    <nav>
      <div>
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
        <Link to="/forgot-password">Forgot Password</Link>
      </div>
    </nav>
  );
}

export default NavBar;
