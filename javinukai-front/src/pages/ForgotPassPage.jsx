import { useForm } from "react-hook-form";
import forgotPassword from "../services/auth/forgotPassword";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import validateEmail from "../utils/validateEmail";
import StyledInput from "../Components/StyledInput";
import FormFieldError from "../Components/FormFieldError";
import { useTranslation } from "react-i18next";

function ForgotPassPage() {
  const {
    reset,
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const { mutate } = useMutation({
    mutationFn: (data) => forgotPassword(data.email),
    onSuccess: () => toast.success(t("ForgotPassPage.resetSuccess")),
    onError: () => toast.error(t("services.forgotPasswordError")),
  });

  function onSubmit(formData) {
    mutate(formData);
    reset();
  }

  const { t } = useTranslation();
  return (
    <div className="w-full min-h-[82vh] flex flex-col justify-center items-center">
      <div className="mx-2 px-6 py-10 rounded bg-white lg:w-1/3 shadow">
        <form onSubmit={handleSubmit(onSubmit)}>
          <h2 className="text text-lg font-semibold">
            {t("ForgotPassPage.title")}
          </h2>
          <p>{t("ForgotPassPage.description")}</p>
          <section className="flex flex-col space-y-4 mt-3">
            <input
              className="px-2 py-1 rounded border-2 border-slate-100"
              id="recovery-email"
              placeholder={t("ForgotPassPage.emailPlaceholder")}
              {...register("email", {
                required: t("ForgotPassPage.emailRequired"),
                validate: (val) =>
                  validateEmail(val) || t("ForgotPassPage.emailValidFormat"),
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
            <StyledInput
              id="submit-email"
              type="submit"
              value={t("ForgotPassPage.submitEmail")}
            />
          </section>
        </form>
      </div>
    </div>
  );
}

export default ForgotPassPage;
