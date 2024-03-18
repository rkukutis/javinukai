import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Layout from "./Layout";
import ContestsPage from "../pages/ContestsPage";
import ContestDetailsPage from "../pages/ContestDetailsPage";
import ImageUpload from "./ImageUpload";
import UserSubmissionView from "./UserSubmissionView";
import JurySubmissionView from "./JurySubmissionView";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import CreateUserPage from "../pages/CreateUserPage";
import ForgotPassPage from "../pages/ForgotPassPage";
import ResetPassPage from "../pages/ResetPassPage";
import ConfirmEmailPage from "../pages/ConfirmEmailPage";
import PersonalInformation from "./user-management/PersonalInformation";
import UserManagementPage from "../pages/UserManagementPage";
import UserDetailsPage from "../pages/UserDetailsPage";
import ContestPage from "../pages/ContestPage";
import CategoryPage from "../pages/CategoryPage";
import PreviewPage from "../pages/PreviewPage";
import ParticipationRequests from "./participation-request-components/ParticipationRequests";
import NotFoundPage from "../pages/NotFoundPage";

function SiteRouter({ user }) {
  const userRole = user?.role;

  // const getRoute = (currentRole, path) => {
  //   if (currentRole === undefined) {
  //     return "/login";
  //   }

  //   if (
  //     (path === "/personal-info") &
  //     (currentRole === "ADMIN" ||
  //       currentRole === "MODERATOR" ||
  //       currentRole === "USER")
  //   ) {
  //     return path;
  //   }

  //   if (
  //     (path === "category/:categoryId/upload") &
  //     (currentRole === "ADMIN" ||
  //       currentRole === "MODERATOR" ||
  //       currentRole === "USER")
  //   ) {
  //     return path;
  //   }

  //   if (
  //     (path === "category/:categoryId/my-entries") &
  //     (currentRole === "ADMIN" ||
  //       currentRole === "MODERATOR" ||
  //       currentRole === "USER")
  //   ) {
  //     return path;
  //   }

  //   if ((path === "/create-user") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/requests") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/manage-users") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/manage-users/:userId") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/contest-page") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/category-page") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if ((path === "/preview-page") & (currentRole === "ADMIN")) {
  //     return path;
  //   }
  //   if (
  //     (path === "/contest/:contestId/category/:categoryId/contestant-entries") &
  //     (currentRole === "JURY")
  //   ) {
  //     return path;
  //   }
  // };

  const getRoute = (currentRole, roles, path) => {
    // if (currentRole === undefined) {
    //   return "/login";
    // }

    if (roles.includes(currentRole)) {
      return path;
    }
  };

  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<Navigate to="/contests" />} />
            <Route path="/contests" element={<ContestsPage />} />
            <Route path="/contest/:contestId" element={<ContestDetailsPage />}>
              <Route
                path="category/:categoryId/upload"
                element={<ImageUpload />}
              />
              <Route
                path="category/:categoryId/my-entries"
                element={<UserSubmissionView />}
              />
            </Route>

            {/* <Route
              path="/contest/:contestId/category/:categoryId/contestant-entries"
              element={<JurySubmissionView />}
            /> */}
            {/* <Route
              path={getRoute(
                userRole,
                "/contest/:contestId/category/:categoryId/contestant-entries"
              )}
              element={<JurySubmissionView />}
            /> */}
            <Route
              path={getRoute(
                userRole,
                ["JURY"],
                "/contest/:contestId/category/:categoryId/contestant-entries"
              )}
              element={<JurySubmissionView />}
            />

            <Route path="/login" element={<LoginPage />} />
            {/* <Route
              path={getRoute(
                userRole,
                ["JURY", "MODERATOR", "JURY", "USER"],
                "/login"
              )}
              element={<LoginPage />}
            /> */}

            <Route path="/register" element={<RegisterPage />} />

            {/* <Route path="/create-user" element={<CreateUserPage />} /> */}
            {/* <Route
              path={getRoute(userRole, "/create-use")}
              element={<CreateUserPage />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN"], "/create-user")}
              element={<CreateUserPage />}
            />

            <Route path="/forgot-password" element={<ForgotPassPage />} />
            <Route path="/reset-password" element={<ResetPassPage />} />
            <Route path="/confirm-email" element={<ConfirmEmailPage />} />

            {/* <Route path="/personal-info" element={<PersonalInformation />} /> */}
            {/* <Route
              path={getRoute(userRole, "/personal-info")}
              element={<PersonalInformation />}
            /> */}
            <Route
              path={getRoute(
                userRole,
                ["ADMIN", "MODERATOR", "USER"],
                "/personal-info"
              )}
              element={<PersonalInformation />}
            />

            {/* <Route path="/manage-users" element={<UserManagementPage />} />
            <Route path="/manage-users/:userId" element={<UserDetailsPage />} /> */}
            <Route
              path={getRoute(userRole, "/manage-users")}
              element={<UserManagementPage />}
            />
            {/* <Route
              path={getRoute(userRole, ["ADMIN"], "/manage-users")}
              element={<UserManagementPage />}
            /> */}
            {/* <Route
              path={getRoute(userRole, "/manage-users/:userId")}
              element={<UserDetailsPage />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN"], "/manage-users/:userId")}
              element={<UserDetailsPage />}
            />

            {/* <Route path="/contest-page" element={<ContestPage />} />
            <Route path="/category-page" element={<CategoryPage />} />
            <Route path="Preview-page" element={<PreviewPage />} /> */}
            {/* <Route
              path={getRoute(userRole, "/contest-page")}
              element={<ContestPage />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN"], "/contest-page")}
              element={<ContestPage />}
            />
            {/* <Route
              path={getRoute(userRole, "/category-page")}
              element={<CategoryPage />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN"], "/category-page")}
              element={<CategoryPage />}
            />
            {/* <Route
              path={getRoute(userRole, "/preview-page")}
              element={<PreviewPage />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN", "MODERATOR"], "/preview-page")}
              element={<PreviewPage />}
            />

            {/* <Route path="/requests" element={<ParticipationRequests />} /> */}
            {/* <Route
              path={getRoute(userRole, "/requests")}
              element={<ParticipationRequests />}
            /> */}
            <Route
              path={getRoute(userRole, ["ADMIN"], "/requests")}
              element={<ParticipationRequests />}
            />

            <Route path="/*" element={<NotFoundPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default SiteRouter;
