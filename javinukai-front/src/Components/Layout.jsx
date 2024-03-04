import { Link, Outlet, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import NavBar from "./NavBar";
import useUserStore from "../stores/userStore";
import { useMutation } from "@tanstack/react-query";
import logoutUser from "../services/auth/logoutUser";
import Button from "./Button";
import DropDownMenu from "./DropDownMenu";
import { useTranslation } from "react-i18next";
import LanguageSwitcher from "./LanguageSwitcher";

export default function Layout() {
  const { t } = useTranslation();
  const { user, removeUser } = useUserStore((state) => state);
  const navigate = useNavigate();
  const { mutate } = useMutation({
    mutationFn: () => {
      console.log(user);
      logoutUser();
    },
    onSuccess: () => {
      toast.success(t("layout.loggedOutSuccess"));
      removeUser();
      navigate("/");
    },
    onError: () => toast.error(t("services.logoutUserError")),
  });

  return (
    <div>
      <header className="text min-h-[12vh] bg-slate-700 text-slate-100">
        <div className="flex justify-between items-center px-6 py-2">
          <div>
            <a href="/">{t("layout.name")}</a>
          </div>
          <div className="flex justify-between space-x-4 items-center px-2 py-2">
            <LanguageSwitcher />
            {user ? (
              <DropDownMenu mutationFunction={mutate}></DropDownMenu>
            ) : (
              <Link to="/login">
                <Button>{t("layout.login")}</Button>
              </Link>
            )}
          </div>
        </div>
        {user && <NavBar user={user} />}
      </header>
      <main className="min-h-[82vh]">
        <Outlet />
      </main>
      <footer className="h-[10vh] bg-teal-600 text-slate-100 ">FOOTER</footer>
    </div>
  );
}
