import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import getUser from "../../services/users/getUser";
import SpinnerPage from "../../pages/SpinnerPage";
import Button from "../Button";
import { useTranslation } from "react-i18next";
import useUserStore from "../../stores/userStore";
import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import updateUserDetails from "../../services/users/updateUserDetails";
import ChangePassword from "/src/Components/user-management/ChangePassword"
import { useForm } from "react-hook-form";
import FormFieldError from "../../Components/FormFieldError";


function PersonalInformation() {
  const { t } = useTranslation();
  const { user } = useUserStore((state) => state);
  const [data, setData] = useState();
  const { register, handleSubmit, formState: { errors } } = useForm();
  const { data: userData, isFetching, refetch } = useQuery({
    queryKey: ["user", user.id],
    queryFn: () => getUser(user.id),
  });

  const [isEditing, setIsEditing] = useState(false);
  const [editedData, setEditedData] = useState({});
  const queryClient = useQueryClient();

  useEffect(() => {
    setData(userData);
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
    if (fieldName === 'isFreelance') {
      const parsedValue = value === 'true';
      setEditedData(prevData => ({
        ...prevData,
        [fieldName]: parsedValue,
        institution: parsedValue ? '' : prevData.institution
      }));
    } else {
      setEditedData(prevData => ({
        ...prevData,
        [fieldName]: value
      }));
    }
  };

  const onSubmit = async () => {
    try {
      if (!editedData.isFreelance && !editedData.institution) {
        toast.error("Please enter the institution name.");
        return;
      }

      if (!editedData.name.trim()) {
        toast.error("Fields cannot be empty.");
        return;
      }

      const updatedData = { ...editedData, uuid: data.uuid };
      await updateUserMutation.mutateAsync(updatedData);
      await refetch();
      setIsEditing(false);
      setData(updatedData);
      toast.success("Changes saved successfully.");
    } catch (error) {
      console.error(error);
      toast.error("An error occurred while saving changes.");
    }
  };
  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-100">
          <div className="lg:w-3/4 w-full h-fit bg-white shadow-md lg:my-4 p-8 rounded-md">
            <article className="lg:grid flex flex-col space-y-4 lg:space-y-0 pb-4">
              <form onSubmit={handleSubmit(onSubmit)}>
                <section>
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalName")}
                  </label>
                  <span>: </span>
                  {isEditing ? (
                    <>
                      <input
                        id="name"
                        className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                        value={editedData.name || ""}
                        onChange={(e) => handleChange("name", e.target.value)}
                        {...register("name", {
                          required: 
                          t("UserDetailsPage.nameRequired"),
                        })}
                      />
                      {errors.name && (
                        <FormFieldError>{errors.name.message}</FormFieldError>
                      )}
                    </>
                  ) : (
                    <span className="text-lg text-wrap text-teal-600">
                      {isEditing ? editedData.name : data?.name}
                    </span>
                  )}
                </section>
              </form>
              <section>
                <label className="text-lg text-slate-900">
                  {t("UserDetailsPage.personalSurname")}
                </label>
                <span>: </span>
                {isEditing ? (
                  <input
                    type="text"
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={isEditing ? editedData.surname : data?.surname}
                    onChange={(e) => handleChange("surname", e.target.value)}
                  />
                ) : (
                  <span className="text-lg text-wrap text-teal-600">
                    {isEditing ? editedData.surname : data?.surname}
                  </span>
                )}
              </section>
              <section>
                <label className="text-lg text-slate-900">
                  {t("UserDetailsPage.personalBirthYear")}
                </label>
                <span>: </span>
                {isEditing ? (
                  <input
                    type="text"
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={isEditing ? editedData.birthYear : data?.birthYear}
                    onChange={(e) => handleChange("birthYear", e.target.value)}
                  />
                ) : (
                  <span className="text-lg text-wrap text-teal-600">
                    {isEditing ? editedData.birthYear : data?.birthYear}
                  </span>
                )}
              </section>
              <section>
                <label className="text-lg text-slate-900">
                  {t("UserDetailsPage.personalPhoneNumber")}
                </label>
                <span>: </span>
                {isEditing ? (
                  <input
                    type="text"
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={
                      isEditing ? editedData.phoneNumber : data?.phoneNumber
                    }
                    onChange={(e) =>
                      handleChange("phoneNumber", e.target.value)
                    }
                  />
                ) : (
                  <span className="text-lg text-wrap text-teal-600">
                    {isEditing ? editedData.phoneNumber : data?.phoneNumber}
                  </span>
                )}
              </section>
              <section>
                <label className="text-lg text-slate-900">
                  {t("UserDetailsPage.personalEmail")}
                </label>
                <span>: </span>
                {isEditing ? (
                  <input
                    type="text"
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={isEditing ? editedData.email : data?.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                  />
                ) : (
                  <span className="text-lg text-wrap text-teal-600">
                    {isEditing ? editedData.email : data?.email}
                  </span>
                )}
              </section>
              <section className="text py-3">
                <label className="text-lg text-slate-900">
                  {t("UserDetailsPage.personalFreelance")}
                </label>
                <span>: </span>
                {isEditing ? (
                  <select
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={editedData.isFreelance}
                    onChange={(e) =>
                      handleChange("isFreelance", e.target.value)
                    }
                    required
                  >
                    <option value={true}>True</option>
                    <option value={false}>False</option>
                  </select>
                ) : (
                  <span className="text-lg text-wrap text-teal-600">
                    {data?.isFreelance ? "True" : "False"}
                  </span>
                )}
              </section>
              {!isEditing && data?.isFreelance === false && (
                <section>
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalInstitution")}
                  </label>
                  <span>: </span>
                  <span className="text-lg text-wrap text-teal-600">
                    {data?.institution}
                  </span>
                </section>
              )}
              {isEditing && editedData.isFreelance === false && (
                <section>
                  <label className="text-lg text-slate-900">
                    {t("UserDetailsPage.personalInstitution")}
                  </label>
                  <span>: </span>
                  <input
                    type="text"
                    className="text-lg text-wrap text-teal-600 border-b border-teal-600"
                    value={editedData.institution}
                    onChange={(e) =>
                      handleChange("institution", e.target.value)
                    }
                  />
                </section>
              )}
            </article>

            <Button onClick={isEditing ? handleSubmit(onSubmit) : handleEdit}>
              {isEditing ? "Save" : "Edit"}
            </Button>
            {isEditing && <Button onClick={handleCancel}>Cancel</Button>}

            <ChangePassword />
          </div>
        </div>
      )}
    </>
  );
}

export default PersonalInformation;