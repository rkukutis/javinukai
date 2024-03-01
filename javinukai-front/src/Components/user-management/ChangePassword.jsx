import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { useTranslation } from "react-i18next";
import { useNavigate, useSearchParams } from "react-router-dom";
import FormFieldError from "../FormFieldError";
import StyledInput from "../StyledInput";
import Button from "../Button";
import changePassword from "../../services/auth/changePassword";
import axios from "axios";

function ChangePassword() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const {
    reset,
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  // const { mutate } = useMutation({
  //   mutationFn: (formData, userData, t) =>
  //     changePassword(formData, userData, t),
  //   onSuccess: () => {
  //     toast.success(t("RegisterPage.registrationSuccess"));
  //     navigate("/");
  //   },
  //   onError: (err) => {
  //     toast.error(err.message);
  //     reset();
  //   },
  // });

  function onSubmit(formData) {

    console.log("onSubmit token", token);

    axios
      .post("http://localhost:8080/api/v1/change-password", {
        resetToken: token,
        newPassword: "123456789",
      })
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => console.log(error));

    // if (!token) {
    //   toast.error(t("ResetPassPage.passChangeError"));
    //   return;
    // }
    // mutate({ data: { token, newPassword: formData.newPassword }, t });

    console.log("form data -> ", formData);
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center justify-center">
      <div className="p-3 w-full lg:w-2/5">
        <form
          id="change-password-form"
          className="flex flex-col space-y-2 bg-slate-50 p-2 rounded-sm"
          onSubmit={handleSubmit(onSubmit)}
        >
          <section className="form-field">
            <label>{t("ChangeUsersPasswordForm.oldPassword")}</label>
            <input
              type="password"
              id="old-password"
              className="form-field__input"
              {...register("oldPassword", {
                required: {
                  value: true,
                  message: t("RegisterPage.passwordRequired"),
                },
                minLength: {
                  value: 8,
                  message: t("RegisterPage.passwordValid"),
                },
              })}
            />
            {errors.oldPassword && (
              <FormFieldError>{errors.oldPassword.message}</FormFieldError>
            )}
          </section>

          <section className="form-field">
            <label>{t("ChangeUsersPasswordForm.newPassword")}</label>
            <input
              type="password"
              id="new-password"
              className="form-field__input"
              {...register("newPassword", {
                required: {
                  value: true,
                  message: t("RegisterPage.passwordRequired"),
                },
                minLength: {
                  value: 8,
                  message: t("RegisterPage.passwordValid"),
                },
              })}
            />
            {errors.newPassword && (
              <FormFieldError>{errors.newPassword.message}</FormFieldError>
            )}
          </section>

          <section className="form-field">
            <label>
              {t("ChangeUsersPasswordForm.newPasswordConfirmation")}
            </label>
            <input
              type="password"
              id="new-password-confirm"
              className="form-field__input"
              {...register("newPasswordConfirm", {
                required: t("RegisterPage.passwordConfirmRequired"),
                validate: (value) => {
                  if (watch("newPassword") != value) {
                    return t("RegisterPage.passwordConfirmMustMatch");
                  }
                },
              })}
            />
            {errors.newPasswordConfirm && (
              <FormFieldError>
                {errors.newPasswordConfirm.message}
              </FormFieldError>
            )}
          </section>

          <StyledInput
            id="change-password-submit"
            value={t("ChangeUsersPasswordForm.confirmChangePasswordButton")}
            type="submit"
          />
          <Button />
        </form>
      </div>
    </div>
  );
}

export default ChangePassword;
