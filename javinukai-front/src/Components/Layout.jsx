import { Link, Outlet, useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import useUserStore from "../stores/userStore";
import { useMutation } from "@tanstack/react-query";
import logoutUser from "../services/auth/logoutUser";
import Button from "./Button";
import DropDownMenu from "./DropDownMenu";
import { useTranslation } from "react-i18next";
import LanguageSwitcher from "./LanguageSwitcher";
import logo from "../assets/logo.jpg";
import PageContent from "./PageContent";

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
      <header className="text min-h-fit bg-slate-700 text-slate-100">
        <div className="flex justify-between items-center px-6 py-2">
          <div className="p-2">
            <Link className="flex items-center hover:scale-105" to="/">
              <img className="h-16 hidden md:block pr-4" src={logo} />
              <p className="border-l-2 w-2 pl-4 font-semibold hidden md:block">
                {t("layout.name")}
              </p>
            </Link>
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
      </header>
      <main>
        <PageContent>
          <Outlet />
        </PageContent>
      </main>
      <footer className="min-h-[10vh] bg-slate-600 text-slate-100 flex items-center justify-center">
        {t("layout.adminContact")}: ricardas.kukutis@stud.techin.lt
      </footer>
    </div>
  );
}
