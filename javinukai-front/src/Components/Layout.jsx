import { Link, Outlet, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import NavBar from "./NavBar";
import useUserStore from "../stores/userStore";
import { useMutation } from "@tanstack/react-query";
import logoutUser from "../services/auth/logoutUser";
import Button from "./Button";
import DropDownMenu from "./DropDownMenu";

export default function Layout() {
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
      <header className="text min-h-[12vh] bg-slate-700 text-slate-100">
        <div className="flex justify-between items-center px-6 py-2">
          <a href="/">WEBSITE NAME</a>
          {user ? (
            <div>
              <DropDownMenu mutationFunction={mutate}></DropDownMenu>
            </div>
          ) : (
            <Link to="/login">
              <Button>Log In</Button>
            </Link>
          )}
        </div>
        {user && <NavBar />}
      </header>
      <main className="min-h-[82vh]">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-teal-600 text-slate-100 ">FOOTER</footer>
    </div>
  );
}
