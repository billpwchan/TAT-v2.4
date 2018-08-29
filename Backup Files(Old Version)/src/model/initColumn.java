/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.CaseExecutions;
import DB.Iterations;
import DB.Requirement;
import DB.Script;
import DB.TestCampaign;
import DB.TestCase;
import DB.TestStep;
import controller.tabtestexecution.TabViewResultsController;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;

/**
 *
 * @author tmorin
 */
public class initColumn {

    private TabViewResultsController mainViewResultController = null;

    ObservableList<String> testlist = FXCollections.observableArrayList("NOK", "OKWC", "OK", "Not testable", "Incomplete", "OS", "NExec");

    /**
     *
     */
    public initColumn() {

    }

    /**
     *
     * @param mainViewResultController
     */
    public void setMainController(TabViewResultsController mainViewResultController) {
        this.mainViewResultController = mainViewResultController;
    }

    /**
     *
     * @param tableViewTestCase
     */
    public void initColumnCase(TableView<TestCase> tableViewTestCase) {

        /*
         First column of the view/creation case.
         */
        TableColumn caseID = new TableColumn();
        TableColumn caseVersion = new TableColumn();
        TableColumn project = new TableColumn();
        TableColumn typeOfTest = new TableColumn();
        TableColumn categoryOfTest = new TableColumn();
        TableColumn location = new TableColumn();

        /*
         Second column of the view/creation case.
         */
        TableColumn titleCase = new TableColumn();
        TableColumn testMethodIadt = new TableColumn();
        TableColumn writingStatus = new TableColumn();
        TableColumn writer = new TableColumn();
        TableColumn writerEmail = new TableColumn();
        TableColumn testCaseSource = new TableColumn();

        /*
         Third column of the view/creation case.
         */
        TableColumn environment = new TableColumn();
        TableColumn creationDate = new TableColumn();
        TableColumn editionDate = new TableColumn();
        TableColumn caseInternalVersion = new TableColumn();
        TableColumn totalSteps = new TableColumn();
        TableColumn blocking = new TableColumn();

        /**
         * Text Area of test case.
         */
        TableColumn testObjective = new TableColumn();
        TableColumn internalComments = new TableColumn();

        /*
         Default non visible columns
         */
        writer.setVisible(false);
        caseInternalVersion.setVisible(false);
        creationDate.setVisible(false);
        editionDate.setVisible(false);
        testMethodIadt.setVisible(false);
        environment.setVisible(false);
        testObjective.setVisible(false);
        writerEmail.setVisible(false);
        writingStatus.setVisible(false);

        /*
         Name the column of the table test case
         */
        caseID.setText("Test case ID");
        caseVersion.setText("Test case version");
        project.setText("Project");
        typeOfTest.setText("Type of test");
        categoryOfTest.setText("Category of test");
        location.setText("Location");

        titleCase.setText("Test case title");
        testMethodIadt.setText("Test method (IADT)");
        writingStatus.setText("Writing status");
        writer.setText("Writer");
        writerEmail.setText("Writer email");
        testCaseSource.setText("Test case source");

        environment.setText("Environment");
        creationDate.setText("Creation date");
        editionDate.setText("Edition date");
        caseInternalVersion.setText("Test case internal version");
        totalSteps.setText("Total test steps");
        blocking.setText("Blocking");

        testObjective.setText("Test objectives");
        internalComments.setText("Internal comments");

        /*
         Link the column to the test case object value.
         */
        caseID.setCellValueFactory(new PropertyValueFactory<>("testCaseIdentification"));
        caseVersion.setCellValueFactory(new PropertyValueFactory<>("testCaseVersion"));
        project.setCellValueFactory(new PropertyValueFactory<>("project"));
        typeOfTest.setCellValueFactory(new PropertyValueFactory<>("typeOfTest"));
        categoryOfTest.setCellValueFactory(new PropertyValueFactory<>("categoryOfTest"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        titleCase.setCellValueFactory(new PropertyValueFactory<>("caseTitle"));
        testMethodIadt.setCellValueFactory(new PropertyValueFactory<>("testMethodIadt"));
        writingStatus.setCellValueFactory(new PropertyValueFactory<>("writingStatus"));
        writer.setCellValueFactory(new PropertyValueFactory<>("writter"));
        writerEmail.setCellValueFactory(new PropertyValueFactory<>("writterEmail"));
        testCaseSource.setCellValueFactory(new PropertyValueFactory<>("testCaseSource"));

        environment.setCellValueFactory(new PropertyValueFactory<>("environment"));
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        editionDate.setCellValueFactory(new PropertyValueFactory<>("editionDate"));
        caseInternalVersion.setCellValueFactory(new PropertyValueFactory<>("caseInternalVersion"));
        totalSteps.setCellValueFactory(new PropertyValueFactory<>("totalSteps"));
        blocking.setCellValueFactory(new PropertyValueFactory<>("blocking"));

        testObjective.setCellValueFactory(new PropertyValueFactory<>("testObjective"));
        internalComments.setCellValueFactory(new PropertyValueFactory<>("internalComments"));

        /**
         * Add all columns
         */
        tableViewTestCase.getColumns().addAll(caseID,
                caseVersion,
                project,
                typeOfTest,
                categoryOfTest,
                location,
                titleCase,
                testMethodIadt,
                writingStatus,
                writer,
                writerEmail,
                testCaseSource,
                environment,
                creationDate,
                editionDate,
                caseInternalVersion,
                totalSteps,
                blocking,
                testObjective,
                internalComments);
        //tableViewTestCase.getColumns().add(lastNameColumn);
//        description.setCellFactory(new Callback<TableColumn<TestCase, String>, TableCell<TestCase, String>>() {
//            @Override
//            public TableCell<TestCase, String> call(TableColumn<TestCase, String> p) {
//                return new TableCell<TestCase, String>() {
//                    @Override
//                    public void updateItem(String t, boolean empty) {
//                        super.updateItem(t, empty);
//                        //System.out.println("String to tooltip: "+t);
//                        if (t == null) {
//                            setTooltip(null);
//                            setText(null);
//                        } else if (getTableRow() != null) {
//                            TestCase myModel = tableViewTestCase.getItems().get(getTableRow().getIndex());
//                            Tooltip tooltip = new Tooltip();
//                            tooltip.setText(myModel.getDescription());
//                            //System.out.println(myModel);
//                            setTooltip(tooltip);
//                            setText(t.toString());
//                        }
//                    }
//                };
//            }
//        });
    }

    /**
     *
     * @param tableViewTestCampaign
     */
    public void initColumnCampaign(TableView<TestCampaign> tableViewTestCampaign) {
        TableColumn reference = new TableColumn();
        TableColumn system = new TableColumn();
        TableColumn writter = new TableColumn();
        TableColumn campaignVersion = new TableColumn();
        TableColumn description = new TableColumn();
        TableColumn comments = new TableColumn();
        TableColumn creationDate = new TableColumn();
        TableColumn editionDate = new TableColumn();
        TableColumn softwareSUTRelease = new TableColumn();
        TableColumn numberTestCase = new TableColumn();
        TableColumn regressionThread = new TableColumn();
        TableColumn writterEmail = new TableColumn();

        writter.setVisible(false);
        campaignVersion.setVisible(false);
        creationDate.setVisible(false);
        editionDate.setVisible(false);
        softwareSUTRelease.setVisible(false);
        regressionThread.setVisible(false);
        writterEmail.setVisible(false);

        reference.setText("Campaign ID");
        system.setText("System");
        writter.setText("Writer");
        campaignVersion.setText("Version Campaign");
        description.setText("Description");
        comments.setText("Comments");
        creationDate.setText("Creation date");
        editionDate.setText("Edition Date");
        softwareSUTRelease.setText("Software SUT Release");
        numberTestCase.setText("Number of test cases");
        regressionThread.setText("Regression Thread");
        writterEmail.setText("Writer Email");

        reference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        writter.setCellValueFactory(new PropertyValueFactory<>("writter"));
        system.setCellValueFactory(new PropertyValueFactory<>("system"));
        campaignVersion.setCellValueFactory(new PropertyValueFactory<>("campaignVersion"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        comments.setCellValueFactory(new PropertyValueFactory<>("comments"));
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        editionDate.setCellValueFactory(new PropertyValueFactory<>("editionDate"));
        softwareSUTRelease.setCellValueFactory(new PropertyValueFactory<>("softwareSutRelease"));
        numberTestCase.setCellValueFactory(new PropertyValueFactory<>("numberTestCase"));
        regressionThread.setCellValueFactory(new PropertyValueFactory<>("regressionThread"));
        writterEmail.setCellValueFactory(new PropertyValueFactory<>("writterEmail"));

        tableViewTestCampaign.getColumns().addAll(reference,
                writter,
                system,
                campaignVersion,
                description,
                creationDate,
                editionDate,
                softwareSUTRelease,
                numberTestCase,
                regressionThread,
                writterEmail,
                comments);
        //tableViewTestCase.getColumns().add(lastNameColumn);
        description.setCellFactory(new Callback<TableColumn<TestCampaign, String>, TableCell<TestCampaign, String>>() {
            @Override
            public TableCell<TestCampaign, String> call(TableColumn<TestCampaign, String> p) {
                return new TableCell<TestCampaign, String>() {
                    @Override
                    public void updateItem(String t, boolean empty) {
                        super.updateItem(t, empty);
                        //System.out.println("String to tooltip: "+t);
                        if (t == null) {
                            setTooltip(null);
                            setText(null);
                        } else if (getTableRow() != null) {
                            TestCampaign myModel = tableViewTestCampaign.getItems().get(getTableRow().getIndex());
                            Tooltip tooltip = new Tooltip();
                            tooltip.setText(myModel.getDescription());
                            //System.out.println(myModel);
                            setTooltip(tooltip);
                            setText(t);
                        }
                    }
                };
            }
        });
    }

    /**
     *
     * @param tableViewTestCase
     */
    public void initColumnCaseToExecute(TableView<CaseExecutions> tableViewTestCase) {

        TableColumn<CaseExecutions, String> caseID = new TableColumn<>("CaseID");
        caseID.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestCaseIdentification()));

        TableColumn<CaseExecutions, String> result = new TableColumn<>("Result");
        result.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> p.getValue().simpleStringResultProperty());

        TableColumn<CaseExecutions, String> executionComments = new TableColumn<>("Execution Comments");
        executionComments.setCellValueFactory(new Callback<CellDataFeatures<CaseExecutions, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CaseExecutions, String> p) {
                if (p.getValue().getCaseExecutionResultObj() != null) {
//                    SimpleStringProperty simpleStringComment = new SimpleStringProperty(this, "comment");
//                    simpleStringComment.set(p.getValue().getCaseExecutionResultObj().simpleStringCommentProperty().get()+"\n toto");
//                    return simpleStringComment;
//                    if()
                    //SimpleStringProperty toto = new SimpleStringProperty();
                    if (p.getValue().getCaseExecutionResultObj().getComment() != null && !p.getValue().getCaseExecutionResultObj().getComment().equals("")) {
                        System.out.println("Comment = " + p.getValue().getCaseExecutionResultObj().OldCommentStringProperty() + " Comment2= " + (p.getValue().getCaseExecutionResultObj()));
                        p.getValue().getCaseExecutionResultObj().OldCommentStringProperty().set(p.getValue().getCaseExecutionResultObj().getComment());
                        return p.getValue().getCaseExecutionResultObj().OldCommentStringProperty().concat("\n").concat(p.getValue().getCaseExecutionResultObj().simpleStringCommentProperty());
                    } else {
                        System.out.println("Comment3= " + p.getValue().getCaseExecutionResultObj().getSimpleStringCommentProperty());
                        return p.getValue().getCaseExecutionResultObj().simpleStringCommentProperty();

                    }

                } else {
                    return new ReadOnlyObjectWrapper("");
                }
            }
        });

        if (mainViewResultController != null) {
            result.setCellFactory(new Callback<TableColumn<CaseExecutions, String>, TableCell<CaseExecutions, String>>() {
                @Override
                public TableCell<CaseExecutions, String> call(TableColumn<CaseExecutions, String> param) {
                    TableCell<CaseExecutions, String> cell = new TableCell<CaseExecutions, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            if (item != null && item != "NExec") {
                                System.out.println("ITEM = " + item);
                                ChoiceBox choice = new ChoiceBox(testlist);
                                choice.getSelectionModel().select(testlist.indexOf(item));
                                setStyle(setStyleColor(item));
                                choice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                                    public void changed(ObservableValue<? extends String> source, String oldValue, String newValue) {
                                        setStyle(setStyleColor(newValue));
                                        //System.out.println("OBJECT = "+ getTableRow().getItem());
                                        CaseExecutions caseexec = (CaseExecutions) getTableRow().getItem();
                                        caseexec.setCaseExecutionResult(newValue);
                                        mainViewResultController.addModifiedCaseExecution(caseexec);
                                    }
                                }
                                );
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                setGraphic(choice);

                            } else {
                                if (item == "NExec") {
                                    setText(item);
                                    setStyle(" -fx-control-inner-background: #D9D9D9;\n"
                                            + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                                            + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
                                }
                            }
                        }
                    };
                    return cell;
                }
            }
            );

            executionComments.setCellFactory(new Callback<TableColumn<CaseExecutions, String>, TableCell<CaseExecutions, String>>() {
                @Override
                public TableCell<CaseExecutions, String> call(TableColumn<CaseExecutions, String> col) {
                    final TableCell<CaseExecutions, String> cell = new TableCell<CaseExecutions, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? null : item);
                        }
                    };
                    cell.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.getClickCount() > 1) {
                                mainViewResultController.popUpChangeComment((CaseExecutions) cell.getTableRow().getItem());
                                //CaseExecutions caseexec = (CaseExecutions) getTableRow().getItem();
                                //System.out.println("double click on " + cell.getTableRow().getItem());
                            }
                        }
                    });
                    return cell;
                }
            });

        }
//        result.setCellFactory(
//                new Callback<TableColumn<CaseExecutions, String>, TableCell<CaseExecutions, String>>() {
//                    @Override
//                    public TableCell<CaseExecutions, String> call(TableColumn<CaseExecutions, String> param) {
//                        ObservableList<String> testlist = FXCollections.observableArrayList("A", "B", "C");
//                        ChoiceBoxTableCell choice = new ChoiceBoxTableCell(testlist);
//                        choice.setText("D");
//                        return choice;
//
//                    }
//                });
        //        result.setCellFactory(column -> {
//            return new TableCell<CaseExecutions, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        System.out.println("ITEM = null");
//                        setText(null);
//                        setStyle("");
//                    } else if (item.equals("NOK")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
        //result.setCellFactory(ChoiceBoxTableCell.forTableColumn(testlist));
        result.setEditable(
                true);

        //result.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> p.getValue().simpleStringResultProperty());
        TableColumn<CaseExecutions, String> TCVersion = new TableColumn<>("Test Case Version");

        TCVersion.setCellValueFactory(
                (CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestCaseVersion()));

        TableColumn<CaseExecutions, String> project = new TableColumn<>("Project");

        project.setCellValueFactory(
                (CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getProject()));

        TableColumn<CaseExecutions, String> typeOfTest = new TableColumn<>("Type of test");

        typeOfTest.setCellValueFactory(
                (CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTypeOfTest()));

        TableColumn<CaseExecutions, String> testCategory = new TableColumn<CaseExecutions, String>("Category of test");

        testCategory.setCellValueFactory(new Callback<CellDataFeatures<CaseExecutions, String>, ObservableValue<String>>() {

            public ObservableValue<String> call(CellDataFeatures<CaseExecutions, String> p) {
                if (p.getValue().getTestCategory() != null) {
                    return new ReadOnlyObjectWrapper(p.getValue().getTestCategory());
                } else {
                    return new ReadOnlyObjectWrapper(p.getValue().getTestCase().getCategoryOfTest());
                }
            }
        }
        );

        TableColumn<CaseExecutions, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new Callback<CellDataFeatures<CaseExecutions, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CaseExecutions, String> p) {
                if (p.getValue().getLocation() != null) {
                    return new ReadOnlyObjectWrapper(p.getValue().getLocation());
                } else {
                    return new ReadOnlyObjectWrapper(p.getValue().getTestCase().getLocation());
                }
            }
        });

//                TableColumn<CaseExecutions, String> executionComments = new TableColumn<>("Execution Comments");;
//        executionComments.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> p.getValue().getCaseExecutionResultObj().simpleStringCommentProperty());
        TableColumn<CaseExecutions, String> caseTitle = new TableColumn<>("Test Case title");
        caseTitle.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestCaseTitle()));

        TableColumn<CaseExecutions, String> testMethod = new TableColumn<>("Test method (IADT)");
        testMethod.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestMethodIadt()));

        TableColumn<CaseExecutions, String> writingStatus = new TableColumn<>("Writing status");
        writingStatus.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getWritingStatus()));

        TableColumn<CaseExecutions, String> writer = new TableColumn<>("Writer");
        writer.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getWritter()));

        TableColumn<CaseExecutions, String> writerMail = new TableColumn<>("Writer email");
        writerMail.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getWritterEmail()));

        TableColumn<CaseExecutions, String> caseSource = new TableColumn<>("Test Case source");
        caseSource.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestCaseSource()));

        TableColumn<CaseExecutions, String> environment = new TableColumn<>("Environment");
        environment.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getEnvironment()));

        TableColumn<CaseExecutions, String> creationDate = new TableColumn<>("Creation date");
        creationDate.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getCreationDate()));

        TableColumn<CaseExecutions, String> editionDate = new TableColumn<>("Edition date");
        editionDate.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getEditionDate()));

        TableColumn<CaseExecutions, String> internalVersion = new TableColumn<>("Internal version");
        internalVersion.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getCaseInternalVersion()));

        TableColumn<CaseExecutions, String> stepNumber = new TableColumn<>("Total Test Steps");
        stepNumber.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTotalSteps()));

        TableColumn<CaseExecutions, String> blocking = new TableColumn<>("Blocking");
        blocking.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getBlocking()));

        TableColumn<CaseExecutions, String> objectives = new TableColumn<>("Test objectives");
        objectives.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getTestObjective()));

        TableColumn<CaseExecutions, String> internalComment = new TableColumn<>("Internal Comment");
        internalComment.setCellValueFactory((CellDataFeatures<CaseExecutions, String> p) -> new ReadOnlyObjectWrapper(p.getValue().getTestCase().getInternalComments()));

        TableColumn<CaseExecutions, String> excelPath = new TableColumn<CaseExecutions, String>("Path Excel File");
        excelPath.setCellValueFactory(new Callback<CellDataFeatures<CaseExecutions, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<CaseExecutions, String> p) {
                return new ReadOnlyObjectWrapper(p.getValue().getExcelPath());
            }
        }
        );

        /*
         Default non visible columns
         */
        writer.setVisible(false);
        internalVersion.setVisible(false);
        creationDate.setVisible(false);
        editionDate.setVisible(false);
        testMethod.setVisible(false);
        environment.setVisible(false);
        objectives.setVisible(false);
        writerMail.setVisible(false);
        writingStatus.setVisible(false);

        tableViewTestCase.getColumns().addAll(caseID, TCVersion, project, typeOfTest, testCategory, location, caseTitle, testMethod, writingStatus, writer, writerMail, caseSource, environment, creationDate, editionDate,
                internalVersion,
                stepNumber,
                blocking,
                objectives,
                internalComment, excelPath, result, executionComments);

        /*
         setCellFactory in order to put the right text and right color in the cells of the column result
         */
//        result.setCellFactory(column -> {
//            return new TableCell<CaseExecutions, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        System.out.println("ITEM = null");
//                        setText(null);
//                        setStyle("");
//                    } else if (item.equals("NOK")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #FF0000;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0; -fx-text-color: black");
//                    } else if (item.equals("OKWC")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #FFC000;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("OK")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #00FF00;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("Not testable")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("Imcomplete")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #D9D9D9;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("OS")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #8DB4E2;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("NExec")) {
//                        System.out.println("HERE TEXT = "+item);
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #D9D9D9;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    }
//                }
//            };
//        });
    }

    /**
     *
     * @param tableViewTestScript
     */
    public void initScriptToExecute(TableView<Script> tableViewTestScript) {

        TableColumn scriptName = new TableColumn();
        TableColumn parameters = new TableColumn();
        TableColumn stimuli = new TableColumn();
        TableColumn result = new TableColumn();

        scriptName.setText("ScriptName");
        parameters.setText("Parameters");
        stimuli.setText("Stimuli");
        result.setText("Result");

        scriptName.setCellValueFactory(new PropertyValueFactory<>("name"));
        parameters.setCellValueFactory(new PropertyValueFactory<>("params"));
        stimuli.setCellValueFactory(new PropertyValueFactory<>("stimuli"));
        result.setCellValueFactory(new PropertyValueFactory<>("result"));

        tableViewTestScript.getColumns().addAll(scriptName,
                parameters, stimuli,
                result);
    }

    /**
     *
     * @param tableViewTestStep
     */
    public void initColumnStepToExecute(TableView<TestStep> tableViewTestStep) {

        TableColumn humanStimuli = new TableColumn();
        TableColumn humanCheck = new TableColumn();
        TableColumn version = new TableColumn();
        TableColumn result = new TableColumn();

        humanStimuli.setText("Stimuli");
        humanCheck.setText("Check");
        version.setText("version");
        result.setText("Result");

        humanStimuli.setCellValueFactory(new PropertyValueFactory<>("humanStimuli"));
        humanCheck.setCellValueFactory(new PropertyValueFactory<>("humanCheck"));
        version.setCellValueFactory(new PropertyValueFactory<>("version"));
        result.setCellValueFactory(new PropertyValueFactory<>("result"));

        tableViewTestStep.getColumns().addAll(humanStimuli,
                humanCheck,
                version,
                result);
    }

    /**
     *
     * @param campaignBaselineAndExecution
     */
    public void prepareTreeViewTabTestExecution(TreeTableView<Iterations> campaignBaselineAndExecution) {
        TreeTableColumn<Iterations, String> typeCol
                = new TreeTableColumn<>("Type");
        typeCol.setPrefWidth(150);

        typeCol.setCellValueFactory(param -> {
            final TreeItem<Iterations> item = param.getValue();
            final Iterations data = (Iterations) item.getValue();
            switch (data.getType()) {
                case "baseline":
                    return new SimpleObjectProperty<>("Baseline");
                case "execution":
                    return new SimpleObjectProperty<>("Execution");
                case "campaign":
                    return new SimpleObjectProperty<>("Campaign");
                default:
                    return null;
            }
        });

        TreeTableColumn<Iterations, String> campaignCol
                = new TreeTableColumn<>("ID");
        campaignCol.setPrefWidth(150);
        campaignCol.setCellValueFactory(param -> {
            final TreeItem<Iterations> item = param.getValue();
            final Iterations data = (Iterations) item.getValue();
            switch (data.getType()) {
                case "campaign":
                    return new SimpleObjectProperty<>(data.getTestCampaign().getReference());
                case "execution":
                    return new SimpleObjectProperty<>(String.valueOf(data.getIterationNumber()));
                case "baseline":
                    return new SimpleObjectProperty<>(data.getBaselineId());
                default:
                    return null;
            }
        });

        TreeTableColumn<Iterations, String> resultCol
                = new TreeTableColumn<>("result");
        resultCol.setPrefWidth(150);
        resultCol.setCellValueFactory(param -> {
            final TreeItem<Iterations> item = param.getValue();
            final Iterations data = (Iterations) item.getValue();
            switch (data.getType()) {
                case "campaign":
                    return new SimpleObjectProperty<>("");
                case "execution":
                    if (data.getIterationResult() != null) {
                        return new SimpleObjectProperty<>(String.valueOf(data.getIterationResult().doubleValue()) + " %");
                    } else {
                        return new SimpleObjectProperty<>("");
                    }
                case "baseline":
                    return new SimpleObjectProperty<>("");
                default:
                    return null;
            }
        });

        TreeTableColumn<Iterations, Integer> statusCol
                = new TreeTableColumn<>("Iteration Number");
        statusCol.setPrefWidth(150);
        statusCol.setCellValueFactory(param -> {
            final TreeItem<Iterations> item = param.getValue();
            final Iterations data = (Iterations) item.getValue();
            if (data.getType().equals("execution")) {
                return new SimpleObjectProperty<>(data.getIterationNumber());
            } else {
                return null;
            }
        });

        TreeTableColumn<Iterations, String> dateCol
                = new TreeTableColumn<>("Last execution date");
        dateCol.setPrefWidth(150);
        dateCol.setCellValueFactory(param -> {
            final TreeItem<Iterations> item = param.getValue();
            final Iterations data = (Iterations) item.getValue();
            //System.out.println("DATA = "+data.getDate());
            if (data.getType().equals("baseline") || data.getType().equals("execution") || data.getType().equals("campaign")) {
                return new SimpleObjectProperty<>(data.getDate());
            } else {
                return null;
            }
        });

        campaignBaselineAndExecution.setRowFactory(new Callback<TreeTableView<Iterations>, TreeTableRow<Iterations>>() {
            @Override
            public TreeTableRow<Iterations> call(TreeTableView<Iterations> param) {
                TreeTableRow<Iterations> treeTableRow = new TreeTableRow<Iterations>() {
                    @Override
                    protected void updateItem(Iterations item, boolean empty) {
                        super.updateItem(item, empty);
                        PseudoClass campaignPseudoClass = PseudoClass.getPseudoClass("campaign");
                        PseudoClass baselinePseudoClass = PseudoClass.getPseudoClass("baseline");
                        pseudoClassStateChanged(campaignPseudoClass, (!empty) && item.getType().equals("campaign"));
                        pseudoClassStateChanged(baselinePseudoClass, (!empty) && item.getType().equals("baseline"));
                    }
                };
                return treeTableRow;
            }
        }
        );
        campaignBaselineAndExecution.getColumns().addAll(campaignCol, typeCol, dateCol, resultCol);

        resultCol.setStyle("-fx-alignment: CENTER;");

//        resultCol.setCellFactory(TreeTableColumn -> {
//            return new TreeTableCell<Iterations, String>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (item == null || empty) {
//                        setText(null);
//                        setStyle("");
//                    } else if (item.equals("NOK")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #FF0000;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0; -fx-text-color: black");
//                    } else if (item.equals("OKWC")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #FFC000;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("OK")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #00FF00;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("Not testable")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("Incomplete")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #D9D9D9;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("OS")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #8DB4E2;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    } else if (item.equals("NExec")) {
//                        setAlignment(Pos.CENTER);
//                        setText(item);
//                        setStyle(" -fx-control-inner-background: #D9D9D9;\n"
//                                + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
//                                + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black");
//                    }
//                }
//            };
//        });
    }

    /**
     *
     * @param tableViewTestCase
     */
    public void initColumnCaseForBaseline(TableView<TestCase> tableViewTestCase) {
        tableViewTestCase.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn caseID = new TableColumn();
        TableColumn caseTitle = new TableColumn();
        TableColumn configured = new TableColumn();

        caseID.setText("Test case ID");
        caseTitle.setText("Title case");
        configured.setText("Configured");

        caseID.setCellValueFactory(new PropertyValueFactory<>("testCaseIdentification"));
        caseTitle.setCellValueFactory(new PropertyValueFactory<>("caseTitle"));
        configured.setCellValueFactory(new PropertyValueFactory<>("simpleStringConfigured"));

        tableViewTestCase.getColumns().addAll(caseID,
                caseTitle, configured);

        configured.setCellFactory(column -> {
            return new TableCell<TestCase, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else if (!item.equals("configured")) {
                        setAlignment(Pos.CENTER);
                        ImageView imageview = new ImageView();
                        imageview.setImage(new Image("/images/notready.png"));
                        setGraphic(imageview);
                    } else if (item.equals("configured")) {
                        setAlignment(Pos.CENTER);
                        ImageView imageview = new ImageView();
                        imageview.setImage(new Image("/images/ready.png"));
                        setGraphic(imageview);
                    }
                }
            };
        });

    }

    /**
     *
     * @param tableViewRequirements
     */
    public void initColumnRequirement(TableView<Requirement> tableViewRequirements) {
        TableColumn requirementID = new TableColumn();
        TableColumn requirementCategory = new TableColumn();
        TableColumn requirementWritter = new TableColumn();
        TableColumn requirementVersion = new TableColumn();
        TableColumn<Requirement, String> requirementDescription = new TableColumn();
        TableColumn requirementCoverage = new TableColumn();
        TableColumn requirementComment = new TableColumn();
        TableColumn requirementIADT = new TableColumn();

        requirementDescription.setCellValueFactory((CellDataFeatures<Requirement, String> param) -> {
            String htmlString = param.getValue().getRequirementText();
            htmlString = htmlString.replaceAll("\\<.*?\\>", "");
            return new SimpleStringProperty(htmlString);
        });

        //requirementID.setVisible(false);
//        requirementTitle.setVisible(true);
//        requirementWritter.setVisible(true);
//        requirementVersion.setVisible(true);
//        requirementDescription.setVisible(true);
//        requirementCoverage.setVisible(true);
//        requirementComment.setVisible(true);
        requirementID.setText("Requirement ID");
        requirementCategory.setText("Category");
        requirementWritter.setText("Writer");
        requirementVersion.setText("Version");
        requirementDescription.setText("Requirement");
        requirementCoverage.setText("Coverage");
        requirementComment.setText("Comments");
        requirementIADT.setText("Test method");

        requirementID.setCellValueFactory(new PropertyValueFactory<>("requirementID"));
        requirementCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        requirementWritter.setCellValueFactory(new PropertyValueFactory<>("writer"));
        requirementVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        //requirementDescription.setCellValueFactory(new PropertyValueFactory<>("requirementText"));
        requirementCoverage.setCellValueFactory(new PropertyValueFactory<>("coverage"));
        requirementComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        requirementIADT.setCellValueFactory(new PropertyValueFactory<>("iadt"));

        tableViewRequirements.getColumns().addAll(
                requirementID,
                requirementCategory,
                requirementWritter,
                requirementVersion,
                requirementDescription,
                requirementCoverage, requirementIADT,
                requirementComment);
    }

    /**
     *
     * @param tableViewScripts
     * @param isScript
     */
    public void initColumnMacros(TableView<Script> tableViewScripts, boolean isScript) {
        TableColumn macroName = new TableColumn();
        TableColumn macroVersion = new TableColumn();
        TableColumn macroCreationDate = new TableColumn();
        TableColumn macroEditionDate = new TableColumn();
        TableColumn macroDescription = new TableColumn();
        TableColumn jarName = new TableColumn();

        macroName.setCellValueFactory(new PropertyValueFactory<>("name"));
        macroVersion.setCellValueFactory(new PropertyValueFactory<>("scriptVersion"));
        macroCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        macroEditionDate.setCellValueFactory(new PropertyValueFactory<>("editionDate"));
        macroDescription.setCellValueFactory(new PropertyValueFactory<>("desciption"));
        jarName.setCellValueFactory(new PropertyValueFactory<>("callName"));

        tableViewScripts.getColumns().addAll(macroName, macroVersion, macroCreationDate, macroEditionDate, macroDescription);
        if (isScript == true) {
            jarName.setText("Jar name");
            tableViewScripts.getColumns().add(jarName);
        }

        macroName.setText("Name");
        macroVersion.setText("Version");
        macroCreationDate.setText("Creation date");
        macroEditionDate.setText("Edition date");
        macroDescription.setText("Description");

    }

    /**
     *
     * @param requirements
     */
    public static void initTableViewRequirement(ListView requirements) {
        requirements.setCellFactory(listView -> {
            ListCell<Requirement> cell = new ListCell<Requirement>() {
                @Override
                public void updateItem(Requirement item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getRequirementID());
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });
    }

    private String setStyleColor(String result) {
        String style = null;
        if (result.equals("NOK")) {
            style = " -fx-control-inner-background: #FF0000;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0; -fx-text-color: black";
        } else if (result.equals("OKWC")) {
            style = " -fx-control-inner-background: #FFC000;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        } else if (result.equals("OK")) {
            style = " -fx-control-inner-background: #00FF00;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        } else if (result.equals("Not testable")) {
            style = " -fx-control-inner-background: linear-gradient(from 0% 0% to 2% 2%, reflect,  #FFFFFF 30%, #D9D9D9 47%);\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        } else if (result.equals("Incomplete")) {
            style = " -fx-control-inner-background: #D9D9D9;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        } else if (result.equals("OS")) {
            style = " -fx-control-inner-background: #8DB4E2;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        } else if (result.equals("NExec")) {
            style = " -fx-control-inner-background: #D9D9D9;\n"
                    + " -fx-background-color: -fx-table-cell-border-color, -fx-control-inner-background;\n"
                    + " -fx-background-insets: 0, 0 0 1 0;-fx-text-color: black";
        }
        return style;
    }

}
