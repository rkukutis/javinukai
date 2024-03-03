export default function (title, description, contestId, categoryId, images) {
  let formData = new FormData();
  formData.append("categoryId", categoryId);
  formData.append("contestId", contestId);
  formData.append("title", title);
  formData.append("description", description);
  images.map((image) => formData.append("image", image));
  return formData;
}
