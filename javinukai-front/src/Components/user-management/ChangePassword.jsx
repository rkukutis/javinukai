import { useMutation } from "@tanstack/react-query";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { useTranslation } from "react-i18next";
import FormFieldError from "../FormFieldError";
import changePassword from "../../services/auth/changePassword";
import Button from "../Button";

function ChangePassword() {
  const { t } = useTranslation();

  const {
    reset,
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();

  const { mutate } = useMutation({
    mutationFn: (formData) => changePassword({ formData, t }),
    onSuccess: () => {
      toast.success(t("ChangeUsersPasswordForm.changePasswordSuccess"));
      reset();
    },
    onError: (err) => {
      toast.error(err.message);
    },
  });

  function onSubmit(formData) {
    mutate(formData);
  }

  return (
    <div className="bg-white w-4/5 flex flex-col items-center justify-center">
      <div className="pt-3 w-full lg:w-100 ">
        <form
          id="change-password-form"
          className="bg-red-50 flex flex-col space-y-2 p-2 rounded-sm"
          onSubmit={handleSubmit(onSubmit)}
        >
          <section className="form-field bg-red-50 ">
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

          <section className="form-field bg-red-50">
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

          <section className="form-field bg-red-50">
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
          <div className="flex justify-evenly">
            <Button
              id="change-password-submit"
              extraStyle="text-lg mt-2 w-full lg:w-fit"
              type="submit"
            >
              {t("ChangeUsersPasswordForm.confirmChangePasswordButton")}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ChangePassword;
