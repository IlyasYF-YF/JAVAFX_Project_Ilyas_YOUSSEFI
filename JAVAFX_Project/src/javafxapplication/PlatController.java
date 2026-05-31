package javafxapplication;

import com.gestion.model.Plat;
import com.gestion.service.PlatService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlatController implements Initializable {

    // ── FORMULAIRE ──────────────────────────────────────────
    @FXML private TextField       tfCode;
    @FXML private TextField       tfNom;
    @FXML private ComboBox<String> cbCategorie;
    @FXML private TextField       tfPrix;
    @FXML private Spinner<Integer> spQuantite;
    @FXML private CheckBox        chkDispo;

    // ── TABLEAU ──────────────────────────────────────────────
    @FXML private TableView<Plat>           tableView;
    @FXML private TableColumn<Plat, Integer> colId;
    @FXML private TableColumn<Plat, String>  colCode;
    @FXML private TableColumn<Plat, String>  colNom;
    @FXML private TableColumn<Plat, String>  colCategorie;
    @FXML private TableColumn<Plat, Double>  colPrix;
    @FXML private TableColumn<Plat, Integer> colQte;
    @FXML private TableColumn<Plat, Boolean> colDispo;

    // ── RECHERCHE ────────────────────────────────────────────
    @FXML private TextField tfRecherche;

    // ── DASHBOARD / STATISTIQUES ─────────────────────────────
  

    // ── SERVICE ─────────────────────────────────────────────
    private final PlatService            service    = new PlatService();
    private final ObservableList<Plat>   listePlats = FXCollections.observableArrayList();

    // ════════════════════════════════════════════════════════
    // INITIALISATION
    // ════════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Colonnes du tableau
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDispo.setCellValueFactory(new PropertyValueFactory<>("disponible"));

        // ComboBox catégories
        cbCategorie.setItems(FXCollections.observableArrayList(
                "Entrée", "Plat", "Dessert", "Boisson"
        ));

        // Spinner quantité (0 → 999, défaut 0)
        spQuantite.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999, 0)
        );

        // Sélection dans le tableau → remplit le formulaire
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, ancien, sel) -> { if (sel != null) remplirFormulaire(sel); }
        );

        chargerTable();
    }

    // ════════════════════════════════════════════════════════
    // BOUTON — Ajouter
    // ════════════════════════════════════════════════════════
    @FXML
    public void onAjouter() {
        List<String> erreurs = validerFormulaire();
        if (!erreurs.isEmpty()) { afficherErreurs(erreurs); return; }

        Plat p = new Plat();
        p.setCode(tfCode.getText().trim());
        p.setNom(tfNom.getText().trim());
        p.setCategorie(cbCategorie.getValue());
        p.setPrix(Double.parseDouble(tfPrix.getText().trim()));
        p.setQuantite(spQuantite.getValue());
        p.setDisponible(chkDispo.isSelected());

        service.ajouter(p);
        afficherSucces("Produit ajouté avec succès !");
        viderFormulaire();
        chargerTable();
    }

    // ════════════════════════════════════════════════════════
    // BOUTON — Modifier
    // ════════════════════════════════════════════════════════
    @FXML
    public void onModifier() {
        Plat sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            afficherAvertissement("Sélection requise",
                    "Veuillez sélectionner un plat dans le tableau.");
            return;
        }

        List<String> erreurs = validerFormulaire();
        if (!erreurs.isEmpty()) { afficherErreurs(erreurs); return; }

        // ── Confirmation avant modification ──────────────────
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer la modification");
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment modifier « " + sel.getNom() + " » ?");

        confirm.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                sel.setCode(tfCode.getText().trim());
                sel.setNom(tfNom.getText().trim());
                sel.setCategorie(cbCategorie.getValue());
                sel.setPrix(Double.parseDouble(tfPrix.getText().trim()));
                sel.setQuantite(spQuantite.getValue());
                sel.setDisponible(chkDispo.isSelected());

                service.modifier(sel);
                afficherSucces("Produit modifié avec succès !");
                viderFormulaire();
                chargerTable();
            }
        });
    }

    // ════════════════════════════════════════════════════════
    // BOUTON — Supprimer
    // ════════════════════════════════════════════════════════
    @FXML
    public void onSupprimer() {
        Plat sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            afficherAvertissement("Sélection requise", "Veuillez sélectionner un produit à supprimer.");
            return;
        }

        // Boîte de confirmation avant suppression
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmer la suppression");
        confirm.setHeaderText(null);
        confirm.setContentText("Voulez-vous vraiment supprimer « " + sel.getNom() + " » ?");

        confirm.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                service.supprimer(sel.getId());
                afficherSucces("Produit supprimé avec succès !");
                viderFormulaire();
                chargerTable();
            }
        });
    }

    // ════════════════════════════════════════════════════════
    // BOUTON — Recherche (onKeyReleased)
    // ════════════════════════════════════════════════════════
    @FXML
    public void onRecherche() {
        String motCle = tfRecherche.getText().trim();
        List<Plat> resultats = motCle.isEmpty()
                ? service.listerTous()
                : service.rechercher(motCle);
        listePlats.setAll(resultats);
        tableView.setItems(listePlats);
    }
    // ════════════════════════════════════════════════════════
    // BOUTON — Actualiser
    // ════════════════════════════════════════════════════════
    @FXML
    public void onActualiser() {
        chargerTable();
    }

    // ════════════════════════════════════════════════════════
    // BOUTON — Vider formulaire
    // ════════════════════════════════════════════════════════
    @FXML
    public void onVider() {
        viderFormulaire();
    }

    // ════════════════════════════════════════════════════════
    // MÉTHODES UTILITAIRES — Tableau & Stats
    // ════════════════════════════════════════════════════════

    private void chargerTable() {
    listePlats.setAll(service.listerTous());
    tableView.setItems(listePlats);
    }

    /** Met à jour les 4 cartes KPI du dashboard. */
    

    // ════════════════════════════════════════════════════════
    // MÉTHODES UTILITAIRES — Formulaire
    // ════════════════════════════════════════════════════════

    private void remplirFormulaire(Plat p) {
        tfCode.setText(p.getCode());
        tfNom.setText(p.getNom());
        cbCategorie.setValue(p.getCategorie());
        tfPrix.setText(String.valueOf(p.getPrix()));
        spQuantite.getValueFactory().setValue(p.getQuantite());
        chkDispo.setSelected(p.isDisponible());
    }

    private void viderFormulaire() {
        tfCode.clear();
        tfNom.clear();
        cbCategorie.setValue(null);
        tfPrix.clear();
        spQuantite.getValueFactory().setValue(0);
        chkDispo.setSelected(false);
        tableView.getSelectionModel().clearSelection();
    }

    // ════════════════════════════════════════════════════════
    // VALIDATION — retourne la liste des messages d'erreur
    // ════════════════════════════════════════════════════════

    private List<String> validerFormulaire() {
        List<String> erreurs = new ArrayList<>();

        if (tfCode.getText().trim().isEmpty())
            erreurs.add("- Le code est obligatoire.");
        if (tfNom.getText().trim().isEmpty())
            erreurs.add("- Le libellé est obligatoire.");
        if (cbCategorie.getValue() == null)
            erreurs.add("- Le type est obligatoire.");
        if (spQuantite.getValue() == null)
            erreurs.add("- La quantité est obligatoire.");
        if (tfPrix.getText().trim().isEmpty()) {
            erreurs.add("- Le prix est obligatoire.");
        } else {
            try {
                Double.parseDouble(tfPrix.getText().trim());
            } catch (NumberFormatException e) {
                erreurs.add("- Le prix doit être un nombre valide.");
            }
        }
        return erreurs;
    }

    // ════════════════════════════════════════════════════════
    // ALERTES JAVAFX
    // ════════════════════════════════════════════════════════

    /** Alerte WARNING avec la liste des erreurs de validation (comme la capture). */
    private void afficherErreurs(List<String> erreurs) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Champs invalides");
        alert.setHeaderText(null);
        alert.setContentText(String.join("\n", erreurs));
        alert.showAndWait();
    }

    /** Alerte WARNING générique (texte libre). */
    private void afficherAvertissement(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Alerte INFORMATION pour les opérations réussies (comme la capture). */
    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}