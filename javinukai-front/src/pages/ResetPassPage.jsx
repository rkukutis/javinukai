import { useForm } from "react-hook-form";
import { useNavigate, useSearchParams } from "react-router-dom";
import resetPassword from "../services/auth/resetPassword";
import StyledInput from "../Components/StyledInput";
import FormFieldError from "../Components/FormFieldError";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import { useTranslation } from "react-i18next";

function ResetPassPage() {
  const { t } = useTranslation();
  
  const {
    watch,
    register,
    reset,
    handleSubmit,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const { mutate } = useMutation({
    mutationFn: (data) => resetPassword(data),
    onSuccess: () => {
      toast.success(t('ResetPassPage.passChangeSuccess'));
      reset();
      navigate("/");
    },
    onError: (err) => {
      toast.error(err.message);
      reset();
    },
  });

  function onSubmit(formData) {
    const token = searchParams.get("token");
    if (!token) {
      toast.error(
        t('ResetPassPage.passChangeError')
      );
      return;
    }
    mutate({data:{ token, newPassword: formData.password }, t});
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center justify-center">
      <form
        className="w-5/6 bg-slate-50 px-3 py-5 flex flex-col space-y-3 lg:w-1/3"
        onSubmit={handleSubmit(onSubmit)}
      >
        <section className="form-field">
          <label>{t('ResetPassPage.passTitle')}</label>
          <input
            type="password"
            id="new-password"
            className="form-field__input"
            {...register("password", {
              required: t('ResetPassPage.passRequired'),
              minLength: {
                value: 8,
                message: t('ResetPassPage.passLength'),
              },
            })}
          />
          {errors.password && (
            <FormFieldError>{errors.password.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t('ResetPassPage.passTitleConfirm')}</label>
          <input
            type="password"
            id="confirm-new-password"
            className="form-field__input"
            {...register("passwordConfirm", {
              required: t('ResetPassPage.passConfirmRequired'),
              minLength: {
                value: 8,
                message: t('ResetPassPage.passLength'),
              },
              validate: (value) => {
                if (watch("password") != value) {
                  return t('ResetPassPage.passMustMatch');
                }
              },
            })}
          />
          {errors.passwordConfirm && (
            <FormFieldError>{errors.passwordConfirm.message}</FormFieldError>
          )}
        </section>
        <StyledInput id="reset-pass-submit" type="submit" value={t('ResetPassPage.passSubmit')} />
      </form>
    </div>
  );
}

export default ResetPassPage;
