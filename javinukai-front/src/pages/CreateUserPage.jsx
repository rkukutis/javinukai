import { useState } from "react";
import { useForm } from "react-hook-form";
import FormFieldError from "../Components/FormFieldError";
import toast from "react-hot-toast";
import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import StyledInput from "../Components/StyledInput";
import validateEmail from "../utils/validateEmail";
import { useTranslation } from "react-i18next";
import createUser from "../services/auth/createUser";

function CreateUserPage() {
  const { t } = useTranslation();
  const [role, setNewtRole] = useState("USER");
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
    mutationFn: (data) => createUser(data),
    onSuccess: () => {
      toast.success(t("CreateUserPage.creationSuccess"));
      reset();
      navigate("/manage-users");
    },
    onError: (err) => {
      toast.error(err.message);
      reset();
    },
  });

  function onSubmit(formData) {
    const updatedFormData = { ...formData, role: role };
    mutate({ userCreationInfo: updatedFormData, t });
  }

  function onAffiliationChange(newAffiliation) {
    setAffiliation(newAffiliation);
    setValue("institution", undefined);
  }

  function handleChangeRole(newRole) {
    setNewtRole(newRole);
    if (newRole === "JURY") {
      setValue("birthYear", new Date().getFullYear());
      setValue("phoneNumber", "+37066677777");
    }
    if (newRole !== "JURY") {
      setValue("birthYear", "");
      setValue("phoneNumber", "");
    }
  }

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center justify-center">
      <div className="p-3 w-full lg:w-2/5">
        <form
          id="login-form"
          className="flex flex-col space-y-2 bg-slate-50 p-2 rounded-sm"
          onSubmit={handleSubmit(onSubmit)}
        >
          <section className="form-field space-y-2">
            <label>{t("CreateUserPage.userTitle")}</label>
            <select
              className="py-2 rounded-sm border-2 border-slate-100"
              id="initial-role-select"
              onChange={(e) => handleChangeRole(e.target.value)}
            >
              <option value="USER">{t("CreateUserPage.user")}</option>
              <option value="JURY">{t("CreateUserPage.jury")}</option>
              <option value="MODERATOR">{t("CreateUserPage.moderator")}</option>
              <option value="ADMIN">{t("CreateUserPage.admin")}</option>
            </select>
          </section>

          <section className="form-field">
            <label>{t("CreateUserPage.name")}</label>
            <input
              id="name"
              className="form-field__input"
              {...register("name", {
                required: {
                  value: true,
                  message: t("CreateUserPage.nameRequired"),
                },
                pattern: {
                  value: /^[a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ]*$/,
                  message: t("CreateUserPage.nameContains"),
                },
                maxLength: {
                  value: 20,
                  message: t("CreateUserPage.nameLength"),
                },
              })}
            />
            {errors.name && (
              <FormFieldError>{errors.name.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>{t("CreateUserPage.surname")}</label>
            <input
              id="surname"
              className="form-field__input"
              {...register("surname", {
                required: {
                  value: true,
                  message: t("CreateUserPage.surnameRequired"),
                },
                pattern: {
                  value: /^[a-zA-ZąčęėįšųūžĄČĘĖĮŠŲŪŽ]*$/,
                  message: t("CreateUserPage.surnameContains"),
                },
                maxLength: {
                  value: 20,
                  message: t("CreateUserPage.surnameLength"),
                },
              })}
            />
            {errors.surname && (
              <FormFieldError>{errors.surname.message}</FormFieldError>
            )}
          </section>

          {role !== "JURY" && (
            <section className="form-field">
              <label>{t("CreateUserPage.birthYear")}</label>
              <input
                id="birth-year"
                className="form-field__input"
                {...register("birthYear", {
                  required: {
                    value: true,
                    message: t("CreateUserPage.birthYearRequired"),
                  },
                  pattern: {
                    value: /^\d{4}$/,
                    message: t("CreateUserPage.birthYearValid"),
                  },
                  max: {
                    value: new Date().getFullYear(),
                    message:
                      t("CreateUserPage.birthYearMax") +
                      " " +
                      new Date().getFullYear(),
                  },
                  min: {
                    value: 1900,
                    message: t("CreateUserPage.birthYearMin"),
                  },
                })}
              />
              {errors.birthYear && (
                <FormFieldError>{errors.birthYear.message}</FormFieldError>
              )}
            </section>
          )}
          {role !== "JURY" && (
            <section className="form-field">
              <label>{t("CreateUserPage.phoneNumber")}</label>
              <input
                id="phone-number"
                className="form-field__input"
                placeholder="+37000000001"
                {...register("phoneNumber", {
                  required: {
                    value: true,
                    message: t("CreateUserPage.phoneNumberRequired"),
                  },
                  pattern: {
                    value: /^\+?\d{9,11}$/,
                    message: t("CreateUserPage.phoneNumberValid"),
                  },
                  maxLength: {
                    value: 16,
                    message: t("CreateUserPage.phoneNumberLength"),
                  },
                })}
              />
              {errors.phoneNumber && (
                <FormFieldError>{errors.phoneNumber.message}</FormFieldError>
              )}
            </section>
          )}

          <section className="form-field">
            <label>{t("CreateUserPage.email")}</label>
            <input
              className="form-field__input"
              {...register("email", {
                required: {
                  value: true,
                  message: t("CreateUserPage.emailRequired"),
                },
                validate: (val) =>
                  validateEmail(val) || t("CreateUserPage.emailValid"),
                maxLength: {
                  value: 200,
                  message: t("CreateUserPage.emailLength"),
                },
              })}
            />
            {errors.email && (
              <FormFieldError>{errors.email.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>{t("CreateUserPage.password")}</label>
            <input
              type="password"
              id="password"
              className="form-field__input"
              {...register("password", {
                required: {
                  value: true,
                  message: t("CreateUserPage.passwordRequired"),
                },
                minLength: {
                  value: 8,
                  message: t("CreateUserPage.passwordValid"),
                },
              })}
            />
            {errors.password && (
              <FormFieldError>{errors.password.message}</FormFieldError>
            )}
          </section>
          <section className="form-field">
            <label>{t("CreateUserPage.passwordConfirm")}</label>
            <input
              type="password"
              id="password-confirm"
              className="form-field__input"
              {...register("passwordConfirm", {
                required: t("CreateUserPage.passwordConfirmRequired"),
                validate: (value) => {
                  if (watch("password") != value) {
                    return t("CreateUserPage.passwordConfirmMustMatch");
                  }
                },
              })}
            />
            {errors.passwordConfirm && (
              <FormFieldError>{errors.passwordConfirm.message}</FormFieldError>
            )}
          </section>

          {role !== "JURY" && (
            <section className="form-field space-y-2">
              <select
                className="py-2 rounded-sm border-2 border-slate-100"
                id="affiliation-select"
                value={affiliation}
                onChange={(e) => onAffiliationChange(e.target.value)}
              >
                <option value="freelance">
                  {t("CreateUserPage.affiliationFreelance")}
                </option>
                <option value="institution">
                  {t("CreateUserPage.affiliationInstitution")}
                </option>
              </select>
              {affiliation === "institution" && (
                <div className="form-field">
                  <label>
                    {t("CreateUserPage.affiliationInstitutionName")}
                  </label>
                  <input
                    id="institution"
                    className="form-field__input"
                    {...register("institution", {
                      required: t(
                        "CreateUserPage.affiliationInstitutionRequired"
                      ),
                      maxLength: {
                        value: 50,
                        message: t(
                          "CreateUserPage.affiliationInstitutionLength"
                        ),
                      },
                    })}
                  />
                  {errors.institution && (
                    <FormFieldError>
                      {errors.institution.message}
                    </FormFieldError>
                  )}
                </div>
              )}
            </section>
          )}

          <StyledInput
            id="registration-submit"
            value={t("CreateUserPage.creationSubmit")}
            type="submit"
          />
        </form>
      </div>
    </div>
  );
}

export default CreateUserPage;
