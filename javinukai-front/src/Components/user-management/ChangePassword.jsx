import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { useTranslation } from "react-i18next";
import {
  useLocation,
  useNavigate,
  useParams,
  useSearchParams,
} from "react-router-dom";
import FormFieldError from "../FormFieldError";
import StyledInput from "../StyledInput";
import changePassword from "../../services/auth/changePassword";
import useUserStore from "../../stores/userStore";
import { useState } from "react";

function ChangePassword() {
  const { t } = useTranslation();

  const location = useLocation();
  const { userData } = location.state;
  console.log("user data in ChangePassword.jsx -> ", userData);

  const {
    // reset,
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const { mutate } = useMutation({
    mutationFn: (formData) =>
      changePassword({userData, formData, t}),
    onSuccess: () => {
      toast.success(t("ChangeUsersPasswordForm.changePasswordSuccess"));
      // reset();
      // navigate("/manage-users");
    },
    onError: (err) => {
      toast.error(err.message);
      // reset();
    },
  });

  function onSubmit(formData) {
    // if (!token) {
    //   toast.error(t("ResetPassPage.passChangeError"));
    //   return;
    // }
    mutate(formData);
  }

  // const location = useLocation();
  // const data = location.state.data;

  // const navigate = useNavigate();
  // const [searchParams] = useSearchParams();

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
        </form>
      </div>
    </div>
  );
}

export default ChangePassword;
