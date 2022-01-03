package Group1.GUI;

import Group1.Environment.Area;
import Group1.FileReader.Scenario;
import Group1.Geometry.LineCut;
import Group1.Geometry.Parallelogram;
import Group1.States.GuardState;
import Interop.Geometry.Point;
import Interop.Percept.Vision.ObjectPerceptType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Map extends JPanel {
    Scenario scenario;
    List<GuardState> guardStates;

    public Map(Scenario scenario, List<GuardState> guardStates){
        this.scenario = scenario;
        this.guardStates = guardStates;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;

        // default walls
        //paintComponentDefaultWallsAroundMap(graphics2D);



        // custom walls from map file
        DrawAreas(graphics2D, this.scenario.getWalls(), Color.black);

        // spawn and target areas
        DrawArea(graphics2D, this.scenario.getSpawnAreaGuards(), Color.red);
        DrawArea(graphics2D, this.scenario.getSpawnAreaIntruders(), Color.cyan);
        DrawArea(graphics2D, this.scenario.getTargetArea(), Color.blue);
        // doors
        DrawAreas(graphics2D, this.scenario.getDoors(), Color.red);
        //windows
        DrawAreas(graphics2D, this.scenario.getWindows(), Color.blue);
        // agents
        DrawGuards(graphics2D);
        //sentry
        DrawAreas(graphics2D, this.scenario.getSentrys(), Color.green);
        //teleport
        //DrawAreas(graphics2D, this.scenario.getTeleports(), Color.gray); //teleport is initialized but is not visible due to drawarea only accepting <Area>
    }


    private void paintComponentDefaultWallsAroundMap(Graphics2D graphics2D) {

        // paint walls around map
        ArrayList<Area> wallsAroundMap = new ArrayList<Area>();
        double wallWidth = 5;
        double x_zero = 0 - 1;
        double y_zero = 0 - 1;
        double x_max = this.scenario.getWidth() + 1;
        double y_max = this.scenario.getHeight() + 1;

        // top wall
        wallsAroundMap.add(new Area(
                new Point(x_zero - wallWidth,y_zero - wallWidth),
                new Point(x_max + wallWidth, y_zero),
                ObjectPerceptType.Wall));
        // bottom wall
        wallsAroundMap.add(new Area(
                new Point(x_zero - wallWidth, y_max),
                new Point(x_max + wallWidth, y_max + wallWidth),
                ObjectPerceptType.Wall));
        // left wall
        wallsAroundMap.add(new Area(
                new Point(x_zero - wallWidth, y_zero),
                new Point(x_zero, y_max),
                ObjectPerceptType.Wall));
        // right wall
        wallsAroundMap.add(new Area(
                new Point(x_max, y_zero),
                new Point(x_max + wallWidth, y_max),
                ObjectPerceptType.Wall));

        DrawAreas(graphics2D, wallsAroundMap, Color.black);
    }

    private void DrawAreas(Graphics2D graphics2D, ArrayList<Area> areas, Color color) {
        for (Area area : areas) {
            DrawArea(graphics2D, area, color);
        }
    }

    private void DrawArea(Graphics2D graphics2D, Area area, Color color) {
        int margin = 0; // 10 looks better, but needs to be fixed in DrawGuards

        int areaWidth = (int) (area.getBottomRight().getX() - area.getTopLeft().getX());
        int areaHeight = (int) (area.getBottomRight().getY() - area.getTopLeft().getY());

        Rectangle rectangle = new Rectangle(
                (int) area.getTopLeft().getX() + margin,
                (int) area.getTopLeft().getY() + margin,
                areaWidth,
                areaHeight
        );

        graphics2D.setPaint(color);
        graphics2D.fill(rectangle);
        graphics2D.draw(rectangle);
    }

    private void DrawLine(Graphics2D graphics2D, LineCut lineCut, Color color) {
        Line2D line = new Line2D.Double(
            lineCut.getStart().getX(),
            lineCut.getStart().getY(),
            lineCut.getEnd().getX(),
            lineCut.getEnd().getY()
        );

        graphics2D.setPaint(color);
        graphics2D.draw(line);
    }


    private void DrawGuards(Graphics2D graphics2D) {
        int margin = 0; // need to use as well OR remove above in drawarea

        for(GuardState gs : this.guardStates) {
            Ellipse2D ell = new Ellipse2D.Double(
                    gs.getLocation().getX() - 1.5,
                    gs.getLocation().getY() - 1.5,
                    3,
                    3
            );

            Line2D viewDirection = new Line2D.Double(
                    gs.getLocation().getX(),
                    gs.getLocation().getY(),
                    gs.getLocation().getX() + 20 * Math.cos(gs.getViewDirection().getRadians()),
                    gs.getLocation().getY() + 20 * Math.sin(gs.getViewDirection().getRadians())
            );

            // todo delete later. Now for testing path
            Parallelogram path = new Parallelogram(
                    new Point(gs.getLocation().getX(), gs.getLocation().getY()),
                    new Point(
                            gs.getLocation().getX() + scenario.getMaxMoveDistanceGuard() * Math.cos(gs.getViewDirection().getRadians()),
                            gs.getLocation().getY() + scenario.getMaxMoveDistanceGuard() * Math.sin(gs.getViewDirection().getRadians())),
                    3);
            for (LineCut line : path.getEdges()) {
                DrawLine(graphics2D, line, Color.red);
            }

            //drawing Rays
            /*
            for (int i=0; i < scenario.getViewRays(); i ++) {
                Line2D rayLines = new Line2D.Double(
                        gs.getLocation().getX(),
                        gs.getLocation().getY(),
                        gs.getLocation().getX() + 10 * Math.cos(gs.getViewDirection().getRadians()- (gs.getRadius() /2 ) + (i * (gs.getRadius() / scenario.getViewRays()))),
                        gs.getLocation().getY() + 10 * Math.sin(gs.getViewDirection().getRadians() - (gs.getRadius() /2 ) + (i * (gs.getRadius() / scenario.getViewRays())))
                );
                graphics2D.draw(rayLines);
            }
            */

            graphics2D.setPaint(Color.BLACK);
            graphics2D.draw(viewDirection);

            graphics2D.fill(ell);
            graphics2D.draw(ell);
        }
    }


}
