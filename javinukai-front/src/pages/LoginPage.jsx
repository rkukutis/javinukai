import { useForm } from "react-hook-form";
import { Link, useNavigate } from "react-router-dom";
import FormFieldError from "../Components/FormFieldError";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import useUserStore from "../stores/userStore";
import StyledInput from "../Components/StyledInput";
import validateEmail from "../utils/validateEmail";
import { useTranslation } from "react-i18next";
import loginUser from "../services/auth/loginUser";

function LoginPage() {
  const { t } = useTranslation();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();

  const setUser = useUserStore((state) => state.setUser);
  const { mutate } = useMutation({
    mutationFn: (data) => loginUser(data),
    onSuccess: (loggedInUser) => {
      toast.success(t("loginPage.loginSuccess"));
      setUser(loggedInUser);
      navigate("/");
    },
    onError: () => toast.error(t("services.loginUserError")),
  });

  const handleLoginSubmit = async (formData) => {
    mutate(formData);
  };

  return (
    <div className="w-full h-[82vh] flex justify-center items-center">
      <div className="bg-white shadow rounded p-6 w-5/6 lg:w-2/5 md:w-1/2">
        <form
          id="login-form"
          className="flex flex-col space-y-5"
          onSubmit={handleSubmit(handleLoginSubmit)}
        >
          <section className="form-field">
            <label className=" text-lg">{t("loginPage.email")}</label>
            <input
              className="form-field__input mt-3 "
              {...register("email", {
                required: t("loginPage.emailRequired"),
                validate: (val) =>
                  validateEmail(val) || t("loginPage.emailValidFormat"),
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
          </section>
          <section className="flex flex-col space-y-1">
            <label className="text text-slate-800 mb-3 text-lg">
              {t("loginPage.password")}
            </label>
            <input
              type="password"
              className="form-field__input"
              {...register("password", {
                required: t("loginPage.passwordRequired"),
              })}
            />
            {errors.password && (
              <FormFieldError>{errors.password.message}</FormFieldError>
            )}
            <div className="flex space-x-4 text-blue-500 text-sm pt-2">
              <Link to="/forgot-password">{t("loginPage.forgotPassword")}</Link>
              <Link to="/register">{t("loginPage.register")}</Link>
            </div>
          </section>
          <StyledInput
            extraStyle="rounded py-2"
            id="login-submit"
            value={t("loginPage.login")}
            type="submit"
          />
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
