import { Link, Outlet, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import NavBar from "./NavBar";
import useUserStore from "../stores/userStore";
import { useMutation } from "@tanstack/react-query";
import logoutUser from "../services/logoutUser";
import Button from "./Button";

function Layout() {
  const { user, removeUser } = useUserStore((state) => state);
  const navigate = useNavigate();
  const { mutate } = useMutation({
    mutationFn: () => {
      console.log(user);
      logoutUser();
    },
    onSuccess: () => {
      toast.success("Logged out successfully");
      removeUser();
      navigate("/");
    },
    onError: (err) => toast.error(err.message),
  });

  return (
    <div>
      <header className="text h-[12vh] bg-slate-700 text-slate-100">
        <div className="flex justify-between items-center px-6 py-2">
          <a href="/">WEBSITE NAME</a>
          {user ? (
            <div>
              <Button onClick={() => mutate()}>Log out</Button>
            </div>
          ) : (
            <Link to="/login">
              <Button>Log In</Button>
            </Link>
          )}
        </div>
        {user && <NavBar />}
      </header>
      <main className="min-h-[82vh] pb-10">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-teal-600 text-slate-100 ">FOOTER</footer>
    </div>
  );
}

export default Layout;
