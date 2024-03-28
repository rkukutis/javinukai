import { useState } from "react";
import { useForm } from "react-hook-form";
import registerUser from "../services/auth/registerUser";
import FormFieldError from "../Components/FormFieldError";
import toast from "react-hot-toast";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import StyledInput from "../Components/StyledInput";
import validateEmail from "../utils/validateEmail";
import { useTranslation } from "react-i18next";

function RegisterPage() {
  const { t } = useTranslation();
  const [affiliation, setAffiliation] = useState("freelance");
  const navigate = useNavigate();
  const {
    reset,
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({ mode: "onBlur" });

  const { mutate } = useMutation({
    mutationFn: (data) => registerUser(data),
    onSuccess: () => {
      toast.success(t("RegisterPage.registrationSuccess"));
      reset();
      navigate("/");
    },
    onError: (err) => {
      toast.error(err.message);
      reset();
    },
  });

  function onSubmit(formData) {
    mutate({ registrationInfo: formData, t });
  }

  function onAffiliationChange(newAffiliation) {
    setAffiliation(newAffiliation);
    setValue("institution", undefined);
  }

  return (
    <div className="p-3 w-full lg:w-2/5 bg-white mt-12 shadow">
      <form
        id="login-form"
        className="flex flex-col space-y-2 p-2 rounded"
        onSubmit={handleSubmit(onSubmit)}
      >
        <section className="form-field">
          <label>{t("RegisterPage.name")}</label>
          <input
            id="name"
            className="form-field__input"
            {...register("name", {
              required: {
                value: true,
                message: t("RegisterPage.nameRequired"),
              },
              pattern: {
                value: /^[a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ]*$/,
                message: t("RegisterPage.nameContains"),
              },
              maxLength: {
                value: 20,
                message: t("RegisterPage.nameLength"),
              },
            })}
          />
          {errors.name && (
            <FormFieldError>{errors.name.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.surname")}</label>
          <input
            id="surname"
            className="form-field__input"
            {...register("surname", {
              required: {
                value: true,
                message: t("RegisterPage.surnameRequired"),
              },
              pattern: {
                value: /^[a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ]*$/,
                message: t("RegisterPage.surnameContains"),
              },
              maxLength: {
                value: 20,
                message: t("RegisterPage.surnameLength"),
              },
            })}
          />
          {errors.surname && (
            <FormFieldError>{errors.surname.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.birthYear")}</label>
          <input
            id="birth-year"
            className="form-field__input"
            {...register("birthYear", {
              required: {
                value: true,
                message: t("RegisterPage.birthYearRequired"),
              },
              pattern: {
                value: /^\d{4}$/,
                message: t("RegisterPage.birthYearValid"),
              },
              max: {
                value: new Date().getFullYear(),
                message:
                  t("RegisterPage.birthYearMax") +
                  " " +
                  new Date().getFullYear(),
              },
              min: {
                value: 1900,
                message: t("RegisterPage.birthYearMin"),
              },
            })}
          />
          {errors.birthYear && (
            <FormFieldError>{errors.birthYear.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.phoneNumber")}</label>
          <input
            id="phone-number"
            className="form-field__input"
            placeholder="+37000000001"
            {...register("phoneNumber", {
              required: {
                value: true,
                message: t("RegisterPage.phoneNumberRequired"),
              },
              pattern: {
                value: /^\+?\d{9,11}$/,
                message: t("RegisterPage.phoneNumberValid"),
              },
              maxLength: {
                value: 16,
                message: t("RegisterPage.phoneNumberLength"),
              },
            })}
          />
          {errors.phoneNumber && (
            <FormFieldError>{errors.phoneNumber.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.email")}</label>
          <input
            className="form-field__input"
            {...register("email", {
              required: {
                value: true,
                message: t("RegisterPage.emailRequired"),
              },
              validate: (val) =>
                validateEmail(val) || t("RegisterPage.emailValid"),
              maxLength: {
                value: 200,
                message: t("RegisterPage.emailLength"),
              },
            })}
          />
          {errors.email && (
            <FormFieldError>{errors.email.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.password")}</label>
          <input
            type="password"
            id="password"
            className="form-field__input"
            {...register("password", {
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
          {errors.password && (
            <FormFieldError>{errors.password.message}</FormFieldError>
          )}
        </section>
        <section className="form-field">
          <label>{t("RegisterPage.passwordConfirm")}</label>
          <input
            type="password"
            id="password-confirm"
            className="form-field__input"
            {...register("passwordConfirm", {
              required: t("RegisterPage.passwordConfirmRequired"),
              validate: (value) => {
                if (watch("password") != value) {
                  return t("RegisterPage.passwordConfirmMustMatch");
                }
              },
            })}
          />
          {errors.passwordConfirm && (
            <FormFieldError>{errors.passwordConfirm.message}</FormFieldError>
          )}
        </section>
        <section className="form-field space-y-2">
          <select
            className="py-2 rounded-sm border-2 border-slate-100"
            id="affiliation-select"
            value={affiliation}
            onChange={(e) => onAffiliationChange(e.target.value)}
          >
            <option value="freelance">
              {t("RegisterPage.affiliationFreelance")}
            </option>
            <option value="institution">
              {t("RegisterPage.affiliationInstitution")}
            </option>
          </select>
          {affiliation === "institution" && (
            <div className="form-field">
              <label>{t("RegisterPage.affiliationInstitutionName")}</label>
              <input
                id="institution"
                className="form-field__input"
                {...register("institution", {
                  required: t("RegisterPage.affiliationInstitutionRequired"),
                  maxLength: {
                    value: 50,
                    message: t("RegisterPage.affiliationInstitutionLength"),
                  },
                })}
              />
              {errors.institution && (
                <FormFieldError>{errors.institution.message}</FormFieldError>
              )}
            </div>
          )}
        </section>
        <StyledInput
          id="registration-submit"
          value={t("RegisterPage.registerSubmit")}
          type="submit"
        />
      </form>
    </div>
  );
}

export default RegisterPage;
