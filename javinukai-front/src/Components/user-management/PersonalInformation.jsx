import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import getUser from "../../services/users/getUser";
import SpinnerPage from "../../pages/SpinnerPage";
import Button from "../Button";
import { useTranslation } from "react-i18next";
import useUserStore from "../../stores/userStore";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import updateUserDetails from "../../services/users/updateUserDetails";
import ChangePassword from "/src/Components/user-management/ChangePassword";
import { useForm } from "react-hook-form";
import FormFieldError from "../../Components/FormFieldError";
import validateEmail from "../../utils/validateEmail";
import { useNavigate } from "react-router-dom";
import logoutUser from "../../services/auth/logoutUser";
import BackButton from "../BackButton";

function PersonalInformation() {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const [showLogoutMessage, setShowLogoutMessage] = useState();
  const { user, removeUser } = useUserStore((state) => state);
  const [data, setData] = useState({});
  const {
    register,
    formState: { errors },
    handleSubmit,
  } = useForm();
  const { data: userData, isFetching } = useQuery({
    queryKey: ["user", user.id],
    queryFn: () => getUser(user.id),
  });

  const [isEditing, setIsEditing] = useState(false);
  const [editedData, setEditedData] = useState({});
  const queryClient = useQueryClient();

  useEffect(() => {
    if (userData) {
      setData({ ...userData });
    }
  }, [userData]);

  const updateUserMutation = useMutation({
    mutationFn: (data) => updateUserDetails(data),
    onSuccess: () => {
      queryClient.invalidateQueries(["user", user.id]);
    },
    onError: (error) => {
      console.error(error);
    },
  });

  const handleEdit = () => {
    setIsEditing(true);
    setEditedData({ ...data });
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditedData({ ...data });
  };

  const handleChange = (fieldName, value) => {
    if (fieldName === "isFreelance") {
      const parsedValue = value === "true";
      setEditedData((prevData) => ({
        ...prevData,
        [fieldName]: parsedValue,
        institution: parsedValue ? "" : prevData.institution,
      }));
    } else {
      setEditedData((prevData) => ({
        ...prevData,
        [fieldName]: value,
      }));
    }
  };

  const onSubmit = async () => {
    try {
      const updatedData = { ...editedData, uuid: data.uuid };
      await updateUserMutation.mutateAsync(updatedData, {
        onSuccess: () => {
          toast.success(t("PersonalInformation.changeSuccess"));
          setIsEditing(false);
          setData(updatedData);
          if (data?.email !== updatedData.email) {
            logoutUser();
            removeUser();
            navigate("/login");
          }
        },
      });
    } catch (error) {
      toast.error(t("PersonalInformation.changeError"));
      console.error("An unexpected error occurred:", error);
    }
  };

  const [isShowPasswordVisible, setIsShowPasswordVisible] = useState(false);

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-100">
          <div className="lg:w-3/4 w-full h-fit bg-white shadow-md lg:my-4 p-8 rounded-md">
            <article className="lg:grid flex flex-col space-y-4 lg:space-y-0 pb-4">
              <h1 className="text-2xl mb-4">
                {t("dropdownMenu.accountSettings")}
              </h1>
              <form onSubmit={handleSubmit(onSubmit)}>
                <section className="mb-4">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalName")}
                  </label>
                  <span>: </span>
                  <div>
                    {isEditing ? (
                      <>
                        <input
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
                          className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                          value={editedData.name || ""}
                          onChange={(e) => handleChange("name", e.target.value)}
                        />
                        {errors.name && (
                          <FormFieldError>{errors.name.message}</FormFieldError>
                        )}
                      </>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data.name}
                      </span>
                    )}
                  </div>
                </section>

                <section className="mb-4">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalSurname")}
                  </label>
                  <span>: </span>
                  <div>
                    {isEditing ? (
                      <>
                        <input
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
                          className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                          value={editedData.surname}
                          onChange={(e) =>
                            handleChange("surname", e.target.value)
                          }
                        />
                        {errors.surname && (
                          <FormFieldError>
                            {errors.surname.message}
                          </FormFieldError>
                        )}
                      </>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.surname}
                      </span>
                    )}
                  </div>
                </section>

                <section className="mb-4">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalBirthYear")}
                  </label>
                  <span>: </span>
                  <div>
                    {isEditing ? (
                      <>
                        <input
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
                          className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                          value={editedData.birthYear}
                          onChange={(e) =>
                            handleChange("birthYear", e.target.value)
                          }
                        />
                        {errors.birthYear && (
                          <FormFieldError>
                            {errors.birthYear.message}
                          </FormFieldError>
                        )}
                      </>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.birthYear}
                      </span>
                    )}
                  </div>
                </section>

                <section className="mb-4">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalPhoneNumber")}
                  </label>
                  <span>: </span>
                  <div>
                    {isEditing ? (
                      <>
                        <input
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
                          className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                          value={editedData.phoneNumber}
                          onChange={(e) =>
                            handleChange("phoneNumber", e.target.value)
                          }
                        />
                        {errors.phoneNumber && (
                          <FormFieldError>
                            {errors.phoneNumber.message}
                          </FormFieldError>
                        )}
                      </>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.phoneNumber}
                      </span>
                    )}
                  </div>
                </section>

                <section className="mb-4">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalEmail")}
                  </label>
                  <span>: </span>
                  <div
                    onFocus={() => setShowLogoutMessage(true)}
                    onBlur={() => setShowLogoutMessage(false)}
                  >
                    {isEditing ? (
                      <>
                        <input
                          {...register("email", {
                            required: {
                              value: true,
                              message: t("RegisterPage.emailRequired"),
                            },
                            validate: (val) =>
                              validateEmail(val) ||
                              t("RegisterPage.emailValid"),
                            maxLength: {
                              value: 200,
                              message: t("RegisterPage.emailLength"),
                            },
                          })}
                          className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                          value={editedData.email}
                          onChange={(e) =>
                            handleChange("email", e.target.value)
                          }
                        />
                        {showLogoutMessage && (
                          <p className="text-sm text-red-600">
                            {t("PersonalInformation.emailLogoutMessage")}
                          </p>
                        )}
                        {errors.email && (
                          <FormFieldError>
                            {errors.email.message}
                          </FormFieldError>
                        )}
                      </>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.email}
                      </span>
                    )}
                  </div>
                </section>

                <section>
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.accountRole")}
                  </label>
                  <span>: </span>
                  <div>
                    <span className="text-lg text-wrap text-teal-600">
                      {t(`roles.${data?.role}`)}
                    </span>
                  </div>
                </section>

                <section className="text py-3">
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalFreelance")}
                  </label>
                  <span>: </span>
                  <div>
                    {isEditing ? (
                      <select
                        className="text-lg text-wrap text-teal-600 border-teal-600 form-field__input"
                        value={editedData.isFreelance}
                        onChange={(e) =>
                          handleChange("isFreelance", e.target.value)
                        }
                        required
                      >
                        <option value={true}>
                          {t("PersonalInformation.isFreelanceTrue")}
                        </option>
                        <option value={false}>
                          {t("PersonalInformation.isFreelanceFalse")}
                        </option>
                      </select>
                    ) : (
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.isFreelance
                          ? t("PersonalInformation.isFreelanceTrue")
                          : t("PersonalInformation.isFreelanceFalse")}
                      </span>
                    )}
                  </div>
                </section>

                {!isEditing && data?.isFreelance === false && (
                  <section>
                    <label className="text-lg text-slate-900">
                      {t("UserDetailsPage.personalInstitution")}
                    </label>
                    <span>: </span>
                    <div>
                      <span className="text-lg text-wrap text-teal-600">
                        {data?.institution}
                      </span>
                    </div>
                  </section>
                )}

                {isEditing && editedData.isFreelance === false && (
                  <section>
                    <label className="text-lg text-slate-900">
                      {t("UserDetailsPage.personalInstitution")}
                    </label>
                    <span>: </span>
                    <div>
                      {isEditing ? (
                        <>
                          <input
                            {...register("institution", {
                              required: {
                                value: true,
                                message: t(
                                  "RegisterPage.affiliationInstitutionRequired"
                                ),
                              },
                              maxLength: {
                                value: 50,
                                message: t(
                                  "RegisterPage.affiliationInstitutionNameLength"
                                ),
                              },
                            })}
                            className="text-lg text-wrap text-teal-600 border-b border-teal-600 form-field__input"
                            value={editedData.institution || ""}
                            onChange={(e) =>
                              handleChange("institution", e.target.value)
                            }
                          />
                          {errors.institution && (
                            <FormFieldError>
                              {errors.institution.message}
                            </FormFieldError>
                          )}
                        </>
                      ) : (
                        <span className="text-lg text-wrap text-teal-600">
                          {data?.institution}
                        </span>
                      )}
                    </div>
                  </section>
                )}
              </form>
            </article>

            <div className="flex gap-4 py-3">
              <Button onClick={isEditing ? handleSubmit(onSubmit) : handleEdit}>
                {isEditing
                  ? t("PersonalInformation.saveButton")
                  : t("PersonalInformation.editButton")}
              </Button>
              {isEditing && (
                <Button onClick={handleCancel}>
                  {t("PersonalInformation.cancelButton")}
                </Button>
              )}
            </div>

            <div className="">
              <Button
                onClick={() => setIsShowPasswordVisible(!isShowPasswordVisible)}
                extraStyle="text-lg mt-2 w-full lg:w-fit"
              >
                {t("UserDetailsPage.changePasswordButton")}
              </Button>
              {isShowPasswordVisible && <ChangePassword />}
            </div>
            <BackButton extraStyle="mt-2 mx-0" />
          </div>
        </div>
      )}
    </>
  );
}

export default PersonalInformation;
